package com.springboot.MyTodoList.controller;

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
import com.springboot.MyTodoList.service.ToDoItemService;
import com.springboot.MyTodoList.util.BotCommands;
import com.springboot.MyTodoList.util.BotMessages;

/*
 * CaseNumber 1
 * 
 * The user is already logged in and it's chat id is in the database.
 * Message: "Welcome Name"
 * Deploy 4 boxes to show the possible actions
 * "Show tasks, edit task, create task, delete task"
 * 
 * CaseNumber 2
 * 
 * SHOW TASK CASE
 * 
 * CaseNumber 3
 * 
 * EDIT TASK CASE
 * 
 * CaseNumber 4
 * 
 * CREATE TASK CASE
 * 
 * CaseNumber 5
 * 
 * DELETE TASK CASE
 * 
 */



public class ToDoItemBotController extends TelegramLongPollingBot {

	private static final Logger logger = LoggerFactory.getLogger(ToDoItemBotController.class);
	private ToDoItemService toDoItemService;
	private TelegramUserService telegramUserService;
	private TaskService taskService;
	private String botName;

	public ToDoItemBotController(String botToken, String botName, ToDoItemService toDoItemService, TelegramUserService telegramUserService, TaskService taskService) {
		super(botToken);
		logger.info("Bot Token: " + botToken);
		logger.info("Bot name: " + botName);
		this.toDoItemService = toDoItemService;
		this.telegramUserService = telegramUserService;
		this.taskService = taskService;
		this.botName = botName;
	}

	@Override
	public void onUpdateReceived(Update update) {

		if (update.hasMessage() && update.getMessage().hasText()) {

			String messageTextFromTelegram = update.getMessage().getText();
			Long chatId = update.getMessage().getChatId();
			int caseNumber = 0;
			TelegramUser telegramUser = new TelegramUser();

			// If the bot detects the start command
			// "/start"
			if(messageTextFromTelegram.equals(BotCommands.START_COMMAND.getCommand())){

				SendMessage messageToTelegram = new SendMessage();
				messageToTelegram.setChatId(chatId);
				messageToTelegram.setText(BotMessages.WELCOME_MESSAGE.getMessage());
				
				try{
					execute(messageToTelegram);

					// Check if the chatId exists in the database
					if(existByChatId(chatId).getBody() == true){
						// You have successfully logged in
						sendMessage(BotMessages.LOG_IN_SUCCESS.getMessage(), chatId);
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
			else if(messageTextFromTelegram.substring(0, 10).equals(BotCommands.RESPONSE_COMMAND.getCommand())){
				
				// Extracts the User name from the message
				String responseFromUser = messageTextFromTelegram.substring(10,messageTextFromTelegram.length());
				
				SendMessage messageToTelegram = new SendMessage();
				messageToTelegram.setChatId(chatId);
				messageToTelegram.setText("Verifying the user: " + responseFromUser);					
				
				// Verify Telegram User Name from database and get Telegram User Id
				telegramUser.setID(getTelegramUserId(responseFromUser).getBody());
				telegramUser.setChatId(chatId);

				try{
					execute(messageToTelegram);
					if(telegramUser.getID() != null){
						sendMessage(BotMessages.LOG_IN_SUCCESS.getMessage(), telegramUser.getChatId());
						
						// Update Telegram User ChatId					
						ResponseEntity<String> response = updateChatId(telegramUser.getID(), telegramUser.getChatId());
						sendMessage(response.getBody(), telegramUser.getChatId());
						caseNumber = 1;
					}
					else{
						sendMessage(BotMessages.LOG_IN_FAIL.getMessage(), telegramUser.getChatId());
					}
				}
				catch(TelegramApiException e){
					logger.error(e.getLocalizedMessage(), e);
				}				
			}



















	}
}
	
	@Override
	public String getBotUsername() {		
		return botName;
	}

	// Verify Telegram User Name from database
	public ResponseEntity<Boolean> existByChatId(Long chatId){
		return ResponseEntity.ok(telegramUserService.existByChatId(chatId));
	}

	// Get Telegram User Id from database
	public ResponseEntity<Long> getTelegramUserId(String TelegramName){
		return ResponseEntity.ok(telegramUserService.findTelegramUserId(TelegramName));
	}


	// Get Chat Id from database
    public ResponseEntity<Long> findChatId(@PathVariable Long id){
        return ResponseEntity.ok(telegramUserService.findChatIdByTelegramUserId(id));
    }

	// Put Telegram User ChatId
    public ResponseEntity<String> updateChatId(Long id, Long chatId){
		return ResponseEntity.ok(telegramUserService.updateChatId(id, chatId));
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
}