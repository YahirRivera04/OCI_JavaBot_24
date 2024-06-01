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

import com.springboot.MyTodoList.model.TelegramUser;
import com.springboot.MyTodoList.model.UpdateType;

// Task Needs
import com.springboot.MyTodoList.model.Project;
import com.springboot.MyTodoList.model.Sprint;
import com.springboot.MyTodoList.model.TaskStatus;
import com.springboot.MyTodoList.model.Team;
import com.springboot.MyTodoList.model.TeamType;
import com.springboot.MyTodoList.model.Task;
import com.springboot.MyTodoList.service.TaskService;

import java.lang.ModuleLayer.Controller;
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
	private UpdateTypeController updateTypeController;
	private TeamTypeController teamTypeController;
	private TeamController teamController;
	private String botName;

	public ToDoItemBotController(String botToken, String botName, 
	TelegramUserController telegramUserController, TaskService taskService,
	UserTypeController userTypeController,TaskStatusController taskStatusController, 
	ProjectController projectController, SprintController sprintController, 
	UpdateTypeController updateTypeController, TeamTypeController teamTypeController, 
	TeamController teamController) {
		super(botToken);
		logger.info("Bot Token: " + botToken);
		logger.info("Bot name: " + botName);
		this.telegramUserController = telegramUserController;
		this.userTypeController = userTypeController;
		this.taskService = taskService;
		this.taskStatusController = taskStatusController;
		this.projectController = projectController;
		this.sprintController = sprintController;
		this.updateTypeController = updateTypeController;
		this.teamTypeController = teamTypeController;
		this.teamController = teamController;
		this.botName = botName;
	}

	// Set Auxiliar Variable to log in
	int caseNumber = 0;
	// New List of Team Type
	List<TeamType> teamTypeList = List.of(new TeamType());
	// New List of Team
	List<Team> teamList = List.of(new Team());
	// New List of User Type
	List<UserType> userTypeList = List.of(new UserType());
	// New Telegram User Object
	TelegramUser telegramUser = new TelegramUser();
	List<TelegramUser> telegramUserList = List.of(new TelegramUser());
	// New List of Task Status
	List<TaskStatus> taskStatusList = List.of(new TaskStatus());
	// New List of Projects
	List<Project> projectList = List.of(new Project());
	// New List of Sprints
	List<Sprint> sprintList = List.of(new Sprint());
	// New List of Update Type
	List<UpdateType> updateTypeList = List.of(new UpdateType());

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
						}
					}
					else if(messageTextFromTelegram.equals(BotCommands.LOG_OUT_COMMAND.getCommand())){
						caseNumber = 0;
						// Log Out Message
						sendMessage(BotMessages.LOG_OUT_MESSAGE.getMessage(), telegramUser.getChatId());
					}
					else{
						caseNumber++;
						// Continue Message /continue
						sendMessage(BotMessages.CONTINUE_MESSAGE.getMessage(), telegramUser.getChatId());
					}
					break;
				// Next buttons menu to do some actions based on selected for Developers
				case 2:
					// Set information form db to models 
					userTypeList = userTypeController.findAllUserType().getBody();
					taskStatusList = taskStatusController.findAllTaskStatus().getBody();
					projectList = projectController.findAllProjects().getBody();
					sprintList = sprintController.findAllSprints().getBody();
					updateTypeList = updateTypeController.findAllUpdateType().getBody();
					teamTypeList = teamTypeController.findAllTeamType().getBody();
					teamList = teamController.findAllTeams().getBody();

					telegramUserList = telegramUserController.findAllTelegramUsers().getBody();

					// Print all info form models
					sendMessage("Test of User Type", telegramUser.getChatId());
					if(userTypeList != null){
						for(int i = 0; i < userTypeList.size(); i++){
							sendMessage(userTypeController.printUserTypeList(userTypeList.get(i)), telegramUser.getChatId());
						}
					}
					
					sendMessage("Test of Task Status", telegramUser.getChatId());
					if(taskStatusList != null){
						for(int i = 0; i < taskStatusList.size(); i++){
							sendMessage(taskStatusController.printTaskStatusList(taskStatusList.get(i)), telegramUser.getChatId());
						}
					}
					
					sendMessage("Test of Project", telegramUser.getChatId());
					if(projectList != null){
						for(int i = 0; i < projectList.size(); i++){
							sendMessage(projectController.printProjectList(projectList.get(i)), telegramUser.getChatId());
						}
					}

					sendMessage("Test of Sprint", telegramUser.getChatId());
					if(sprintList != null && projectList != null){
						for(int i = 0; i < sprintList.size(); i++){
							for(int j = 0; j < projectList.size(); j++){
								if(sprintList.get(i).getProject().getID() == projectList.get(j).getID()){
									sprintList.get(i).setProject(projectList.get(j));
								}
							}
							sendMessage(sprintController.printSprintList(sprintList.get(i)), telegramUser.getChatId());
						}
					}

					sendMessage("Test of Update Type", telegramUser.getChatId());
					if(updateTypeList != null){
						for(int i = 0; i < updateTypeList.size(); i++){
							sendMessage(updateTypeController.printUpdateTypeList(updateTypeList.get(i)), telegramUser.getChatId());
						}
					}

					sendMessage("Test of Team Type", telegramUser.getChatId());
					if(teamTypeList != null){
						for(int i = 0; i < teamTypeList.size(); i++){
							sendMessage(teamTypeController.printTeamTypeList(teamTypeList.get(i)), telegramUser.getChatId());
						}
					}

					sendMessage("Test of Team", telegramUser.getChatId());
					if(teamList != null && teamTypeList != null){
						for(int i = 0; i < teamList.size(); i++){
							for(int j = 0; j < teamTypeList.size(); j++){
								if(teamList.get(i).getTeamType().getID() == teamTypeList.get(j).getID()){
									teamList.get(i).setTeamType(teamTypeList.get(j));
								}
							}
							sendMessage(teamController.printTeamTypeList(teamList.get(i)), telegramUser.getChatId());
						}
					}

					sendMessage("Test Telegram Users", telegramUser.getChatId());
					if(telegramUserList != null){
						for(int i = 0; i < telegramUserList.size(); i++){
							sendMessage(telegramUserController.printTelegramUserList(telegramUserList.get(i)), telegramUser.getChatId());
						}
					}

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
}