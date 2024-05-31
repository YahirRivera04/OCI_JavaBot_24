package com.springboot.MyTodoList.controller;

import org.apache.tomcat.jni.User;
import org.aspectj.weaver.ast.And;
import org.mockito.internal.matchers.Null;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.springboot.MyTodoList.service.TaskService;
import com.springboot.MyTodoList.service.ToDoItemService;
import com.springboot.MyTodoList.util.BotCommands;
import com.springboot.MyTodoList.util.BotLabels;
import com.springboot.MyTodoList.util.BotMessages;

import io.swagger.models.Response;

import com.springboot.MyTodoList.service.TelegramUserService;
import com.springboot.MyTodoList.model.TelegramUser;

import com.springboot.MyTodoList.model.UserType;
import com.springboot.MyTodoList.service.UserTypeService;

import com.springboot.MyTodoList.model.TaskStatus;
import com.springboot.MyTodoList.service.TaskStatusService;

import java.util.ArrayList;
import java.util.List;


public class ToDoItemBotController extends TelegramLongPollingBot {

	private static final Logger logger = LoggerFactory.getLogger(ToDoItemBotController.class);
	private ToDoItemService toDoItemService;
	private TelegramUserService telegramUserService;
	private UserTypeService userTypeService;
	private TaskStatusService taskStatusService;
	private TaskService taskService;
	private String botName;

	public ToDoItemBotController(String botToken, String botName, TelegramUserService telegramUserService, ToDoItemService toDoItemService, TaskService taskService, UserTypeService userTypeService, TaskStatusService taskStatusService) {
		super(botToken);
		logger.info("Bot Token: " + botToken);
		logger.info("Bot name: " + botName);
		this.toDoItemService = toDoItemService;
		this.telegramUserService = telegramUserService;
		this.userTypeService = userTypeService;
		this.taskService = taskService;
		this.taskStatusService = taskStatusService;
		this.botName = botName;
	}

	@Override
	public void onUpdateReceived(Update update) {

		if (update.hasMessage() && update.getMessage().hasText()) {

			String messageTextFromTelegram = update.getMessage().getText();
			// Get the Telegram Chat Id from Telegram
			Long chatId = update.getMessage().getChatId();

			// Set Auxiliar Variable to iog in
			int caseNumber = 0;

			// New UserType Objects
			UserType userTypeManager = new UserType();
			UserType userTypeDeveloper = new UserType();	

			// Get User Type Information
			userTypeManager = setUserTypeInfo().get(0);
			userTypeDeveloper = setUserTypeInfo().get(1);

			// New Telegram User Object
			TelegramUser telegramUser = new TelegramUser();
			

			// If the bot detects the start command
			// "/start"
			if(messageTextFromTelegram.equals(BotCommands.START_COMMAND.getCommand()) && caseNumber == 0){

				// Send Welcome Message
				SendMessage messageToTelegram = new SendMessage();
				messageToTelegram.setChatId(chatId);
				messageToTelegram.setText(BotMessages.WELCOME_MESSAGE.getMessage());
				
				try{
					execute(messageToTelegram);

					// Check if the chatId exists in the database
					Long chatIdResponse = findChatIdByChatId(chatId).getBody();
					int chatIdCompare = -1;
					// Compare the chatId from the database with the chatId from the user
					if(chatIdResponse != null )	chatIdCompare = Long.compare(chatIdResponse, chatId);
					// Verify the chatId content
					if(chatIdCompare == 0){
						// You have successfully logged in!!
						sendMessage(BotMessages.LOG_IN_SUCCESS.getMessage(), chatId);
						
						// Set Telegram User Information
						telegramUser = setTelegramUser(chatId, userTypeDeveloper, userTypeManager, "");
						
						// Case Number to acces developer or manager methods
						caseNumber++;
						// Continue Message /continue
						sendMessage(BotMessages.CONTINUE_MESSAGE.getMessage(), chatId);
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
			else if(messageTextFromTelegram.substring(0, 7).equals(BotCommands.RESPONSE_COMMAND.getCommand()) && caseNumber == 0){
				
				// Extracts the User name from the message
				String responseFromUser = messageTextFromTelegram.substring(7,messageTextFromTelegram.length());
				
				SendMessage messageToTelegram = new SendMessage();
				messageToTelegram.setChatId(chatId);
				messageToTelegram.setText("Verifying the user: " + responseFromUser);					
				
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
						
						// Case Number to acces developer or manager methods
						caseNumber++;
						// Continue Message /continue
						sendMessage(BotMessages.CONTINUE_MESSAGE.getMessage(), chatId);

					}
					else {
						sendMessage(BotMessages.LOG_IN_FAIL.getMessage(), chatId);
					}
				}
				catch(TelegramApiException e){
					logger.error(e.getLocalizedMessage(), e);
				}				
			}
			// After log in, menu for Dev and Manager	
			else if(messageTextFromTelegram.equals(BotCommands.CONTINUE_COMMAND.getCommand()) && caseNumber == 1){
					
				// Developer Case
				if(telegramUser.getUserType().getName().equals("Developer")){
					
					// Create variables necessaries to interact with telegram					
					SendMessage messageToTelegram = new SendMessage();
					messageToTelegram.setChatId(telegramUser.getChatId());

					// Message with all the information retrieved form the database
					messageToTelegram.setText("Chat Id " + telegramUser.getChatId().toString() + 
					" \nTelegram User Id " + telegramUser.getID().toString() + 
					" \nUser Type " + telegramUser.getUserType().getName() +
					" \nTelegram Name " + telegramUser.getTelegramName());

					ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
					List<KeyboardRow> keyboard = new ArrayList<>();

					// First Row
					KeyboardRow row = new KeyboardRow();
					row.add(BotLabels.SHOW_TASK.getLabel());
					row.add(BotLabels.EDIT_TASK.getLabel());
					
					// Second Row
					row = new KeyboardRow();
					row.add(BotLabels.DELETE_TASK.getLabel());
					row.add(BotLabels.CREATE_TASK.getLabel());
					keyboard.add(row);
					
					// Add the first row to the keyboard
					keyboard.add(row);
					keyboardMarkup.setKeyboard(keyboard);

					// Add the keyboard markup
					messageToTelegram.setReplyMarkup(keyboardMarkup);

					try {
						execute(messageToTelegram);
					} 
					catch (TelegramApiException e) {
						logger.error(e.getLocalizedMessage(), e);
					}

				}
				// Manager Case
				else if(telegramUser.getUserType().getName().equals("Manager")){
				
				}

			}
			else {
				// Verification of what is inside here
				sendMessage(messageTextFromTelegram, telegramUser.getChatId());
			}
			
		}
	}
	
	@Override
	public String getBotUsername() {		
		return botName;
	}

	// TELEGRAM USER METHODS

	// Verify Telegram Chat Id from database
	public ResponseEntity<Long> findChatIdByChatId(Long chatId){
		Long response = telegramUserService.findChatIdByChatId(chatId);
		return ResponseEntity.ok(response);
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

	// Get Telegram User Name with Telegram User Id
	public ResponseEntity<String> findTelegramNameByTelegramUserId(Long id){
		return ResponseEntity.ok(telegramUserService.findTelegramNameByTelegramUserId(id));
	}

	// USER TYPE METHODS
	public ResponseEntity<Long> findUserTypeByName(String name){
		return ResponseEntity.ok(userTypeService.findUserTypeIdByName(name));
	}

	// TASK STATUS METHODS
	public ResponseEntity<List<TaskStatus>> findTaskStatus(){
		return ResponseEntity.ok(taskStatusService.findAllTaskStatus());
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


	public List<UserType> setUserTypeInfo(){

		UserType Manager = new UserType();
		UserType Developer = new UserType();

		Long manager = findUserTypeByName("Manager").getBody();
		Long developer = findUserTypeByName("Developer").getBody();

		Manager.setID(manager);
		Manager.setName("Manager");
		Manager.setDescription("");

		Developer.setID(developer);
		Developer.setName("Developer");
		Developer.setDescription("");

		return List.of(Manager, Developer);
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
		if(telegramUserName.equals("")) user.setTelegramName(findTelegramNameByTelegramUserId(user.getID()).getBody());
		else user.setTelegramName(telegramUserName);
		
		return user;
	}

}