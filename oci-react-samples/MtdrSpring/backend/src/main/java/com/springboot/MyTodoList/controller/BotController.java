
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

import com.springboot.MyTodoList.util.BotCommands;
import com.springboot.MyTodoList.util.BotLabels;
import com.springboot.MyTodoList.util.BotMessages;

import io.swagger.models.Response;

// Telegram User Needs
import com.springboot.MyTodoList.model.UserType;
import com.springboot.MyTodoList.service.UserTypeService;
import com.springboot.MyTodoList.service.TelegramUserService;
import com.springboot.MyTodoList.model.TelegramUser;
import com.fasterxml.jackson.datatype.jdk8.LongStreamSerializer;

// Task Needs

import com.springboot.MyTodoList.model.Project;
import com.springboot.MyTodoList.service.ProjectService;
import com.springboot.MyTodoList.controller.ProjectController;

import com.springboot.MyTodoList.model.Sprint;
import com.springboot.MyTodoList.service.SprintService;

import com.springboot.MyTodoList.model.TaskStatus;
import com.springboot.MyTodoList.service.TaskStatusService;

import com.springboot.MyTodoList.model.Task;
import com.springboot.MyTodoList.service.TaskService;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.IntStream;


public class BotController extends TelegramLongPollingBot {

	private static final Logger logger = LoggerFactory.getLogger(BotController.class);
	private TelegramUserService telegramUserService;
	private UserTypeService userTypeService;
	private TaskStatusService taskStatusService;
	private TaskService taskService;
	private ProjectService projectService;
	private ProjectController projectController;
	private String botName;

	public BotController(String botToken, String botName, TelegramUserService telegramUserService,
	TaskService taskService, UserTypeService userTypeService, TaskStatusService taskStatusService,
	ProjectService projectService, ProjectController projectController) {
		super(botToken);
		logger.info("Bot Token: " + botToken);
		logger.info("Bot name: " + botName);
		this.telegramUserService = telegramUserService;
		this.userTypeService = userTypeService;
		this.taskService = taskService;
		this.taskStatusService = taskStatusService;
		this.projectService = projectService;
		this.projectController = projectController;
		this.botName = botName;
	}

	// Set Auxiliar Variable to iog in
	int caseNumber = 0;
	// // New UserType Objects
	// UserType userTypeManager = new UserType();
	// UserType userTypeDeveloper = new UserType();	

	// New Telegram User Object
	TelegramUser telegramUser = new TelegramUser();

	@Override
	public void onUpdateReceived(Update update) {

		if (update.hasMessage() && update.getMessage().hasText()) {

			String messageTextFromTelegram = update.getMessage().getText();
			// Get the Telegram Chat Id from Telegram
			Long chatId = update.getMessage().getChatId();
			
			switch (caseNumber) {
				// When already logged
				case 1:
					// After log in, menu for Dev and Manager	
					if(messageTextFromTelegram.equals(BotCommands.CONTINUE_COMMAND.getCommand())){

						// Developer Case
						if(telegramUser.getUserType().getName().equals("Developer")){
							
							// Create variables necessaries to interact with telegram					
							SendMessage messageToTelegram = new SendMessage();
							messageToTelegram.setChatId(telegramUser.getChatId());

							// Message with information retrieved form the database
							messageToTelegram.setText("Welcome " + telegramUser.getTelegramName() + 
							", you are logged as " + telegramUser.getUserType().getName());

							ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
							List<KeyboardRow> keyboard = new ArrayList<>();

							// First Row
							KeyboardRow row = new KeyboardRow();
							row.add(BotLabels.SHOW_TASK.getLabel());
							row.add(BotLabels.EDIT_TASK.getLabel());
							keyboard.add(row);

							// Second Row
							row = new KeyboardRow();
							row.add(BotLabels.DELETE_TASK.getLabel());
							row.add(BotLabels.CREATE_TASK.getLabel());
							keyboard.add(row);
							
							// Set the rows to the keyboard
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

							// Create variables necessaries to interact with telegram					
							SendMessage messageToTelegram = new SendMessage();
							messageToTelegram.setChatId(telegramUser.getChatId());

							// Message with all the information retrieved form the database
							messageToTelegram.setText("Welcome " + telegramUser.getTelegramName() + 
							", you are logged as " + telegramUser.getUserType().getName());

							ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
							List<KeyboardRow> keyboard = new ArrayList<>();

							// First Row
							KeyboardRow row = new KeyboardRow();
							row.add(BotLabels.CREATE_SPRINT.getLabel());
							row.add(BotLabels.CREATE_PROJECT.getLabel());
							keyboard.add(row);

							// Second Row
							row = new KeyboardRow();
							row.add(BotLabels.CREATE_PROJECT.getLabel());
							keyboard.add(row);
							
							// Set the rows to the keyboard
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
					}
					else{
						caseNumber++;	
					}
					break;
				// Next buttons menu to do some actions based on selected for Developers
				case 2:
					// Test of methods to build a task
					sendMessage("Test of Task Status", telegramUser.getChatId());
					printTaskStatusList();

					sendMessage("Test of Project", telegramUser.getChatId());
					printProjectList();

					sendMessage("Test User Type", telegramUser.getChatId());
					printUserTypeList();

					break;
				// Log in by default
				default:
					// If the bot detects the start command
					// "/start"
					if(messageTextFromTelegram.equals(BotCommands.START_COMMAND.getCommand())){

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
								telegramUser = setTelegramUser(chatId, "");
								// Case Number to acces developer or manager methods
								caseNumber++;
								//sendMessage("Case number updated to " + caseNumber, chatId);

								// Continue Message /continue
								sendMessage(BotMessages.CONTINUE_MESSAGE.getMessage(), telegramUser.getChatId());
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
								sendMessage(response.getBody(), chatId);					
								// Set local telegram user
								telegramUser = setTelegramUser(chatId, responseFromUser);
								// Case Number to acces developer or manager methods
								caseNumber++;
								//sendMessage("Case number updated to " + caseNumber, chatId);
								
								// Continue Message /continue
								sendMessage(BotMessages.CONTINUE_MESSAGE.getMessage(), telegramUser.getChatId());
							}
							else {
								// Log in fail message
								sendMessage(BotMessages.LOG_IN_FAIL.getMessage(), chatId);
							}
						}
						catch(TelegramApiException e){
							// Log in fail message
							sendMessage(BotMessages.LOG_IN_FAIL.getMessage(), chatId);
							logger.error(e.getLocalizedMessage(), e);
						}				
					}
					break;
			}
		}
	}
	
	@Override
	public String getBotUsername() {		
		return botName;
	}

	// USER TYPE METHODS ()
	public ResponseEntity<List<UserType>> findAllUserType(){
		return ResponseEntity.ok(userTypeService.findAllUserType());
	}

	// TELEGRAM USER METHODS (Works all)

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


	// TASK STATUS METHODS (Works all)

	// Get all Task Status
	public ResponseEntity<List<TaskStatus>> findAllTaskStatus(){
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
	
	// Print All User Type
	public void printUserTypeList(){
		List<UserType> userTypeList = List.of(new UserType());
		userTypeList = findAllUserType().getBody();
		// Print all information form project
		if(userTypeList != null){
			for(int i = 0; i < userTypeList.size(); i++){
				sendMessage("Id " + userTypeList.get(i).getID().toString() +
				" \nName " + userTypeList.get(i).getName() + 
				" \nDescription " + userTypeList.get(i).getDescription(), telegramUser.getChatId());
			}
		}
	}
	// Get All User Type
	public List<UserType> getUserTypeList(){
		List<UserType> userTypeList = List.of(new UserType());
		userTypeList = findAllUserType().getBody();
		return userTypeList;
	}

	// Set all needed for Telegram User Local
	public TelegramUser setTelegramUser(Long chatId, String telegramUserName){

		// Add db informaton to the local user
		TelegramUser user = new TelegramUser();
		// Get User Type Information
		UserType dev = new UserType();
		UserType man = new UserType();
		
		dev = getUserTypeList().get(1);
		man = getUserTypeList().get(0);
		
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
	
	// Print All Projects
	public void printProjectList(){
		List<Project> projectList = List.of(new Project());
		projectList = projectController.findAllProjects().getBody();
		// Print all information form project
		if(projectList != null){
			for(int i = 0; i < projectList.size(); i++){
				sendMessage("Id " + projectList.get(i).getID().toString() +
				" \nName " + projectList.get(i).getName() + 
				" \nDescription " + projectList.get(i).getDescription(), telegramUser.getChatId());
			}
		}
	}
	
	// Print All Task Status
	public void printTaskStatusList(){
		List<TaskStatus> taskStatusList = List.of(new TaskStatus());
		taskStatusList = findAllTaskStatus().getBody();

		if(taskStatusList != null){
			for(int i = 0; i < taskStatusList.size(); i++){
				sendMessage("Id " + taskStatusList.get(i).getID().toString() + 
				" \nName " +  taskStatusList.get(i).getName() + 
				" \nDescription " + taskStatusList.get(i).getDescription(), telegramUser.getChatId());	
			}
		}
	}

	// Set all needed for Task Local
	// public Task setTask(){
		
	// 	Task task = new Task();
		
		
	// 	return task;
	// }


}