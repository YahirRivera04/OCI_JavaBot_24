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
import com.springboot.MyTodoList.model.UserTeam;
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
	private UserTeamController userTeamController;
	private String botName;

	public ToDoItemBotController(String botToken, String botName, 
	TelegramUserController telegramUserController, TaskService taskService,
	UserTypeController userTypeController,TaskStatusController taskStatusController, 
	ProjectController projectController, SprintController sprintController, 
	UpdateTypeController updateTypeController, TeamTypeController teamTypeController, 
	TeamController teamController, UserTeamController userTeamController) {
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
		this.userTeamController = userTeamController;
		this.botName = botName;
	}

	// Set Auxiliar Variable to log in
	int caseNumber = 0;
	// New List of Team Type
	List<TeamType> teamTypeList = List.of(new TeamType());
	// New List of Team
	List<Team> teamList = List.of(new Team());
	// New List of User Team
	List<UserTeam> userTeamList = List.of(new UserTeam());
	// New List of User Type
	List<UserType> userTypeList = List.of(new UserType());
	// New Telegram User Object
	TelegramUser telegramUser = new TelegramUser();
	// New List of Telegram Users
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
								sendMessage("To continue, please select any option from the buttons.", telegramUser.getChatId());
								caseNumber++;
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
							row.add(BotLabels.SHOW_PROJECT.getLabel());
							row.add(BotLabels.LOGOUT.getLabel());
							keyboard.add(row);
							
							// Set the rows to the keyboard
							keyboardMarkup.setKeyboard(keyboard);

							// Add the keyboard markup
							messageToTelegram.setReplyMarkup(keyboardMarkup);

							try {
								execute(messageToTelegram);
								sendMessage("To continue, please select any option from the buttons.", telegramUser.getChatId());
								caseNumber++;
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
					break;
				// Next buttons menu to do some actions based on selected for Developers
				case 2:
					// Developer Buttons
					if(messageTextFromTelegram.equals(BotMessages.SHOW_TASK_COMMAND_MESSAGE.getMessage())){
						sendMessage("Show Task", telegramUser.getChatId());
					}
					else if(messageTextFromTelegram.equals(BotMessages.EDIT_TASK_COMMAND_MESSAGE.getMessage())){
						sendMessage("Edit Task", telegramUser.getChatId());
					}
					else if(messageTextFromTelegram.equals(BotMessages.DELETE_TASK_COMMAND_MESSAGE.getMessage())){
						sendMessage("Delete Task", telegramUser.getChatId());
					}
					else if(messageTextFromTelegram.equals(BotMessages.CREATE_TASK_COMMAND_MESSAGE.getMessage())){
						sendMessage("Create Task", telegramUser.getChatId());
					}
					// Manager Button
					else if(messageTextFromTelegram.equals(BotMessages.CREATE_SPRINT_COMMAND_MESSAGE.getMessage())){
						sendMessage("Create Sprint", telegramUser.getChatId());
					}
					else if(messageTextFromTelegram.equals(BotMessages.CREATE_PROJECT_COMMAND_MESSAGE.getMessage())){
						sendMessage("Create Project", telegramUser.getChatId());
					}
					else if(messageTextFromTelegram.equals(BotMessages.SHOW_PROJECT_COMMAND_MESSAGE.getMessage())){
						sendMessage("Show Project", telegramUser.getChatId());
					}
					else if(messageTextFromTelegram.equals(BotMessages.LOG_OUT_COMMAND_MESSAGE.getMessage())){
						caseNumber = 0;
						// Log Out Message
						sendMessage(BotMessages.LOG_OUT_MESSAGE.getMessage(), telegramUser.getChatId());
					}
					break;
				// Log in by default
				default:
					// If the bot detects the start command
					// "/start"
					if(messageTextFromTelegram.equals(BotCommands.START_COMMAND.getCommand())){

						// Set information form db to user related models 
						userTypeList = userTypeController.findAllUserType().getBody();
						teamTypeList = teamTypeController.findAllTeamType().getBody();
						teamList = teamController.findAllTeams().getBody();
						userTeamList = userTeamController.findAllUserTeams().getBody();
						telegramUserList = telegramUserController.findAllTelegramUsers().getBody();

						// Set information form db to task related models 
						taskStatusList = taskStatusController.findAllTaskStatus().getBody();
						projectList = projectController.findAllProjects().getBody();
						sprintList = sprintController.findAllSprints().getBody();
						updateTypeList = updateTypeController.findAllUpdateType().getBody();

						// Send Welcome Message
						SendMessage messageToTelegram = new SendMessage();
						messageToTelegram.setChatId(chatId);
						messageToTelegram.setText(BotMessages.WELCOME_MESSAGE.getMessage());
						
						try{
							execute(messageToTelegram);
							// Check if the chatId exists in the database

							int chatIdCompare = -1;
							for(int i = 0; i < telegramUserList.size(); i++){
								// Print Users test
								sendMessage(telegramUserController.printTelegramUserList(telegramUserList.get(i)), chatId); // BORRAR
								// Compare Chat id from Users in the Db
								if(telegramUserList != null) chatIdCompare = Long.compare(telegramUserList.get(i).getChatId(), chatId);
								
								if(chatIdCompare == 0){
									// You have successfully logged in!!
									sendMessage(BotMessages.LOG_IN_SUCCESS.getMessage(), chatId);
									// Set Telegram User Information
									telegramUser = telegramUserList.get(i);
									// Case Number to acces developer or manager methods
									caseNumber++;
									//sendMessage("Case number updated to " + caseNumber, chatId);

									// Continue Message /continue
									sendMessage(BotMessages.CONTINUE_MESSAGE.getMessage(), telegramUser.getChatId());
								}
							}					
						}
						catch(TelegramApiException e){
							// Enter your Telegram Username with format /login:TelegramUsername
							sendMessage(BotMessages.LOG_IN_MESSAGE.getMessage(), chatId);
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

							for(int i = 0; i < telegramUserList.size(); i++){
								if(telegramUserList.get(i).getTelegramName().equals(responseFromUser)){
									// User Found Log in sucess
									sendMessage(BotMessages.LOG_IN_SUCCESS.getMessage(), chatId);
									// Set Telegram User Information
									telegramUser = telegramUserList.get(i);
									telegramUser.setChatId(chatId);
									// Update Chat Id in db
									ResponseEntity<String> response = telegramUserController.updateChatId(telegramUser.getID(), telegramUser.getChatId());
									sendMessage(response.getBody(), telegramUser.getChatId());
									// Case Number to acces developer or manager methods
									caseNumber++;
									//sendMessage("Case number updated to " + caseNumber, chatId);
									
									// Continue Message /continue
									sendMessage(BotMessages.CONTINUE_MESSAGE.getMessage(), telegramUser.getChatId());
								}
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