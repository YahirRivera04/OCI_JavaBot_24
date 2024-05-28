package com.springboot.MyTodoList.controller;

import org.apache.tomcat.jni.User;
import org.aspectj.weaver.ast.And;
import org.mockito.internal.matchers.Null;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


import com.springboot.MyTodoList.model.TelegramUser;
import com.springboot.MyTodoList.service.TaskService;
import com.springboot.MyTodoList.service.TelegramUserService;
import com.springboot.MyTodoList.model.UserType;
import com.springboot.MyTodoList.service.UserTypeService;
import com.springboot.MyTodoList.util.BotCommands;
import com.springboot.MyTodoList.util.BotMessages;


public class ToDoItemBotController extends TelegramLongPollingBot {

	private static final Logger logger = LoggerFactory.getLogger(ToDoItemBotController.class);
	private TelegramUserService telegramUserService;
	private UserTypeService userTypeService;
	private TaskService taskService;
	private String botName;

	public ToDoItemBotController(String botToken, String botName, TelegramUserService telegramUserService,UserTypeService userTypeService, TaskService taskService) {
		super(botToken);
		logger.info("Bot Token: " + botToken);
		logger.info("Bot name: " + botName);
		this.telegramUserService = telegramUserService;
		this.userTypeService = userTypeService;
		this.taskService = taskService;
		this.botName = botName;
	}

	@Override
	public void onUpdateReceived(Update update) {

		if (update.hasMessage() && update.getMessage().hasText()) {

			String messageTextFromTelegram = update.getMessage().getText();
			// Get the Telegram Chat Id from Telegram
			Long chatId = update.getMessage().getChatId();
			// Get the Telegram User Name from Telegram		
			String userName = update.getMessage().getForwardFrom().getUserName();

			// Set Auxiliar Variable to iog in
			int caseNumber = 0;
			// Get all the info from User Type
			UserType userTypeManager = findUserTypeByName("Manager").getBody();
			UserType userTypeDeveloper = findUserTypeByName("Developer").getBody();	

			// New Telegram User Object
			TelegramUser telegramUser = new TelegramUser();

			// If the bot detects the start command
			// "/start"
			if(messageTextFromTelegram.equals(BotCommands.START_COMMAND.getCommand()) && caseNumber == 0){

				SendMessage messageToTelegram = new SendMessage();
				messageToTelegram.setChatId(chatId);

				messageToTelegram.setText(BotMessages.WELCOME_MESSAGE.getMessage());

				try{
					execute(messageToTelegram);
					// Check if the chatId exists in the database
					if(existByChatId(chatId).getBody() != null && existByChatId(chatId).getBody() == true){
						// You have successfully logged in!!
						sendMessage(BotMessages.LOG_IN_SUCCESS.getMessage(), chatId);
						telegramUser = setTelegramUser(chatId, userTypeDeveloper, userTypeManager, userName);
						caseNumber = 1;
					}
					else{
						// Enter your Telegram Username with format /login:TelegramUsername
						sendMessage(BotMessages.LOG_IN_MESSAGE.getMessage(), chatId);
					}
				}
				catch(TelegramApiException e){
					logger.error(e.getLocalizedMessage(), e);
				}

			}
			// If the bot detects the command /login:"TelegramUserName"
			else if(messageTextFromTelegram.substring(0, 10).equals(BotCommands.RESPONSE_COMMAND.getCommand()) && caseNumber == 0){
				
				// Extracts the User name from the message
				String responseFromUser = messageTextFromTelegram.substring(10,messageTextFromTelegram.length());
				
				SendMessage messageToTelegram = new SendMessage();
				messageToTelegram.setChatId(chatId);
				messageToTelegram.setText("Verifying the user: " + responseFromUser);					
				
				// Verify Telegram User Name from database and get Telegram User Id
				//telegramUser.setID(getTelegramUserId(responseFromUser).getBody());

				try{
					execute(messageToTelegram);
					if(getTelegramUserId(responseFromUser).getBody() != null){
						// User Found Log in sucess
						sendMessage(BotMessages.LOG_IN_SUCCESS.getMessage(), chatId);
						// Update Chat Id in db
						ResponseEntity<String> response = updateChatId(getTelegramUserId(responseFromUser).getBody(), chatId);
						sendMessage(response.getBody(), telegramUser.getChatId());
						// Set local telegram user
						telegramUser = setTelegramUser(chatId, userTypeDeveloper, userTypeManager, responseFromUser);
						caseNumber = 1;
					}
					else {
						sendMessage(BotMessages.LOG_IN_FAIL.getMessage(), chatId);
					}
				}
				catch(TelegramApiException e){
					logger.error(e.getLocalizedMessage(), e);
				}				
			}	
			
			
			if(caseNumber == 1 && telegramUser.getUserType().getName() == "Developer"){
				sendMessage("Hello Developper " + telegramUser.getTelegramName().toString(), telegramUser.getChatId());
			}
			else if (caseNumber == 1 && telegramUser.getUserType().getName() == "Manager") {
				sendMessage("Hello Manager " + telegramUser.getTelegramName().toString(), telegramUser.getChatId());

			}

	}
}
	
	@Override
	public String getBotUsername() {		
		return botName;
	}

	// TELEGRAM USER METHODS

	// Verify Telegram Chat Id from database
	public ResponseEntity<Boolean> existByChatId(Long chatId){
		return ResponseEntity.ok(telegramUserService.existByChatId(chatId));
	}

	// Get Telegram User Id with User Name
	public ResponseEntity<Long> getTelegramUserId(String TelegramName){
		return ResponseEntity.ok(telegramUserService.findTelegramUserId(TelegramName));
	}

	// Put Telegram User ChatId with Id
    public ResponseEntity<String> updateChatId(Long id, Long chatId){
		return ResponseEntity.ok(telegramUserService.updateChatId(id, chatId));
    }

	// Get Telegram User Id with Chat Id
	public ResponseEntity<Long> findUserId(Long chatId){
		return ResponseEntity.ok(telegramUserService.findUserIdByChatId(chatId));
	}

	// Get User Type with Id
	public ResponseEntity<Long> findUserTypeId(Long id){
		return ResponseEntity.ok(telegramUserService.findUserTypeId(id));
	}


	// USER TYPE METHODS

	public ResponseEntity<UserType> findUserTypeByName(String name){
		return ResponseEntity.ok(userTypeService.findUserTypeByName(name));
	}



	// Auxiliar Method to print messages
	public void sendMessage(String message, Long chatID){
		SendMessage messageToTelegram = new SendMessage();
		messageToTelegram.setChatId(chatID);
		messageToTelegram.setText(message);
		try{
			execute(messageToTelegram);
		}
		catch(TelegramApiException e){
			logger.error(e.getLocalizedMessage(), e);
		}
	}


	public TelegramUser setTelegramUser(Long chatId, UserType dev, UserType man, String telegramUserName){

		// Add db informaton to the local user
		TelegramUser user = new TelegramUser();
		// Set Chat Id
		user.setChatId(chatId);
		// Set Telegram User Id
		user.setID(findUserId(user.getChatId()).getBody());
		// Set User Type
		if(findUserTypeId(user.getID()).getBody() == dev.getID()) user.setUserType(dev);
		else user.setUserType(man);
		// Set Telegram User Name
		user.setName(telegramUserName);
		return user;
	}

}