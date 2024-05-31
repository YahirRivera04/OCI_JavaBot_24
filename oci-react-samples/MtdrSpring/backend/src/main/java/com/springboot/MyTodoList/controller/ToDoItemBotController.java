
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
import com.fasterxml.jackson.datatype.jdk8.LongStreamSerializer;

// Telegram User Needs
import com.springboot.MyTodoList.model.UserType;
import com.springboot.MyTodoList.controller.UserTypeController;

import com.springboot.MyTodoList.model.TelegramUser;
import com.springboot.MyTodoList.controller.TelegramUserController;

// Task Needs
import com.springboot.MyTodoList.model.Project;
import com.springboot.MyTodoList.controller.ProjectController;

import com.springboot.MyTodoList.model.Sprint;
import com.springboot.MyTodoList.controller.SprintController;

import com.springboot.MyTodoList.model.TaskStatus;
import com.springboot.MyTodoList.controller.TaskStatusController;

import com.springboot.MyTodoList.model.Task;
import com.springboot.MyTodoList.service.TaskService;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.IntStream;

public class ToDoItemBotController extends TelegramLongPollingBot {

	private static final Logger logger = LoggerFactory.getLogger(ToDoItemBotController.class);
	private TelegramUserController telegramUserController;
	private UserTypeController userTypeController;
	private TaskStatusController taskStatusController;
	private TaskService taskService;
	private ProjectController projectController;
	private SprintController sprintController;
	private String botName;

	public ToDoItemBotController(String botToken, String botName, 
	TelegramUserController telegramUserController, TaskService taskService,
	UserTypeController userTypeController,TaskStatusController taskStatusController, 
	ProjectController projectController, SprintController sprintController) {
		super(botToken);
		logger.info("Bot Token: " + botToken);
		logger.info("Bot name: " + botName);
		this.telegramUserController = telegramUserController;
		this.userTypeController = userTypeController;
		this.taskService = taskService;
		this.taskStatusController = taskStatusController;
		this.projectController = projectController;
		this.sprintController = sprintController;
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

							// Third Row
							row = new KeyboardRow();
							row.add(BotLabels.LOGOUT.getLabel());
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
							caseNumber++;
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
							row.add(BotLabels.LOGOUT.getLabel());
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
							caseNumber++;
						}
					}
					else if(messageTextFromTelegram.equals(BotCommands.LOG_OUT_COMMAND.getCommand())){
						caseNumber = 0;
					}
					break;
				// Next buttons menu to do some actions based on selected for Developers
				case 2:
					// Test of methods to build a task
					sendMessage("Test of Task Status", telegramUser.getChatId());
					printTaskStatusList(telegramUser.getChatId());

					sendMessage("Test of Project", telegramUser.getChatId());
					printProjectList(telegramUser.getChatId());

					sendMessage("Test User Type", telegramUser.getChatId());
					printUserTypeList(telegramUser.getChatId());

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
							Long chatIdResponse = telegramUserController.findChatIdByChatId(chatId).getBody();
							int chatIdCompare = -1;
							// Compare the chatId from the database with the chatId from the user
							if(chatIdResponse != null )	chatIdCompare = Long.compare(chatIdResponse, chatId);
							// Verify the chatId content
							if(chatIdCompare == 0){
								// You have successfully logged in!!
								sendMessage(BotMessages.LOG_IN_SUCCESS.getMessage(), chatId);
								// Set Telegram User Information
								telegramUser = telegramUserController.setTelegramUser(chatId, "");
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
							if(telegramUserController.getTelegramUserId(responseFromUser).getBody() != null){
								// User Found Log in sucess
								sendMessage(BotMessages.LOG_IN_SUCCESS.getMessage(), chatId);
								// Update Chat Id in db
								ResponseEntity<String> response = telegramUserController.updateChatId(telegramUserController.getTelegramUserId(responseFromUser).getBody(), chatId);
								sendMessage(response.getBody(), chatId);					
								// Set local telegram user
								telegramUser = telegramUserController.setTelegramUser(chatId, responseFromUser);
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

	// Auxiliar Method to print messages
	public void sendMessage(String message, Long chatID){
		SendMessage messageToTelegram = new SendMessage();
		messageToTelegram.setChatId(chatID);
		messageToTelegram.setText(message);
		try{
			execute(messageToTelegram);
		}
		catch(Exception e){
			logger.error(e.getLocalizedMessage(), e);
		}
	}

	// Print All User Type
    public void printUserTypeList(Long chatId){
        List<UserType> userTypeList = List.of(new UserType());
        userTypeList = userTypeController.findAllUserType().getBody();
        // Print all information form project
        if(userTypeList != null){
            for(int i = 0; i < userTypeList.size(); i++){
                sendMessage("Id " + userTypeList.get(i).getID().toString() +
                " \nName " + userTypeList.get(i).getName() + 
                " \nDescription " + userTypeList.get(i).getDescription(), chatId);
            }
        }
    }

	// Print All Task Status
	public void printTaskStatusList(Long chatId){
		List<TaskStatus> taskStatusList = List.of(new TaskStatus());
		taskStatusList = taskStatusController.findAllTaskStatus().getBody();

		if(taskStatusList != null){
			for(int i = 0; i < taskStatusList.size(); i++){
				sendMessage("Id " + taskStatusList.get(i).getID().toString() + 
				" \nName " +  taskStatusList.get(i).getName() + 
				" \nDescription " + taskStatusList.get(i).getDescription(), chatId);	
			}
		}
	}

	// Print All Projects
    public void printProjectList(Long chatId){
        List<Project> projectList = List.of(new Project());
        projectList = projectController.findAllProjects().getBody();
        // Print all information form project
        if(projectList != null){
            for(int i = 0; i < projectList.size(); i++){
                sendMessage("Id " + projectList.get(i).getID().toString() +
                " \nName " + projectList.get(i).getName() + 
                " \nDescription " + projectList.get(i).getDescription(), chatId);
            }
        }
    }

	// Print All Sprints
	public void printSprintList(Long chatId){
		List<Sprint> sprintList = List.of(new Sprint());
		sprintList = sprintController.findAllSprints().getBody();
		if(sprintList != null){
            for(int i = 0; i < sprintList.size(); i++){
                sendMessage("Id " + sprintList.get(i).getID().toString() +
                " \nName " + sprintList.get(i).getName() + 
                " \nDescription " + sprintList.get(i).getDescription() + 
				" \nStart Date " + sprintList.get(i).getStartDate() + 
				" \nEnd Date " + sprintList.get(i).getEndDate() + 
				" \nProject Id " + sprintList.get(i).getProject().getID(), chatId);
            }
        }
	}

}