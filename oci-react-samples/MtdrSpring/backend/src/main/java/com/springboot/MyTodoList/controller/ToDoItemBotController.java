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
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.sql.Timestamp;

import com.springboot.MyTodoList.util.BotCommands;
import com.springboot.MyTodoList.util.BotLabels;
import com.springboot.MyTodoList.util.BotMessages;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

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
import com.springboot.MyTodoList.model.SprintUpdate;
import com.springboot.MyTodoList.model.TaskStatus;
import com.springboot.MyTodoList.model.TaskUpdate;
import com.springboot.MyTodoList.model.Team;
import com.springboot.MyTodoList.model.TeamType;
import com.springboot.MyTodoList.model.Task;

import java.lang.ModuleLayer.Controller;
import java.util.ArrayList;
import java.util.List;

public class ToDoItemBotController extends TelegramLongPollingBot {

	private static final Logger logger = LoggerFactory.getLogger(ToDoItemBotController.class);
	private TelegramUserController telegramUserController;
	private UserTypeController userTypeController;
	private TaskStatusController taskStatusController;
	private TaskController taskController;
	private ProjectController projectController;
	private SprintController sprintController;
	private UpdateTypeController updateTypeController;
	private TeamTypeController teamTypeController;
	private TeamController teamController;
	private UserTeamController userTeamController;
	private TaskUpdateController taskUpdateController;
	private SprintUpdateController sprintUpdateController;
	private String botName;

	public ToDoItemBotController(String botToken, String botName, 
	TelegramUserController telegramUserController, TaskController taskController,
	UserTypeController userTypeController,TaskStatusController taskStatusController, 
	ProjectController projectController, SprintController sprintController, 
	UpdateTypeController updateTypeController, TeamTypeController teamTypeController, 
	TeamController teamController, UserTeamController userTeamController,
	TaskUpdateController taskUpdateController, SprintUpdateController sprintUpdateController) {
		super(botToken);
		logger.info("Bot Token: " + botToken);
		logger.info("Bot name: " + botName);
		this.telegramUserController = telegramUserController;
		this.userTypeController = userTypeController;
		this.taskController = taskController;
		this.taskStatusController = taskStatusController;
		this.projectController = projectController;
		this.sprintController = sprintController;
		this.updateTypeController = updateTypeController;
		this.teamTypeController = teamTypeController;
		this.teamController = teamController;
		this.userTeamController = userTeamController;
		this.taskUpdateController = taskUpdateController;
		this.sprintUpdateController = sprintUpdateController;
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
	// New List of Tasks
	List<Task> taskList = List.of(new Task());

	@Override
	public void onUpdateReceived(Update update) {

		if (update.hasMessage() && update.getMessage().hasText()) {

			String messageTextFromTelegram = update.getMessage().getText();
			// Get the Telegram Chat Id from Telegram
			Long chatId = update.getMessage().getChatId();
			
			switch (caseNumber) {
				// When already logged
				case 1:
					// Set information form db to task related models 
					taskStatusList = taskStatusController.findAllTaskStatus().getBody();
					projectList = projectController.findAllProjects().getBody();
					sprintList = sprintController.findAllSprints().getBody();
					updateTypeList = updateTypeController.findAllUpdateType().getBody();

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
						sendMessage("Use " + BotCommands.START_COMMAND.getCommand() + " to log in", telegramUser.getChatId());
					}
					break;
				// Next buttons menu to do some actions based on selected for Developers
				case 2:
					// Developer Buttons
					if(messageTextFromTelegram.equals(BotMessages.SHOW_TASK_COMMAND_MESSAGE.getMessage())){
						try{

							// Get Tasks from Data Base
							taskList = taskController.findAllTaskByTelegramUserId(telegramUser.getID()).getBody();				
							// Message header
							sendMessage(BotMessages.SHOW_TASK_MESSAGE.getMessage(),telegramUser.getChatId());
							// Show all tasks that belongs to the user
							for(int i = 0; i < taskList.size(); i++){
								sendMessage(taskController.printTask(taskList.get(i)), telegramUser.getChatId());
							}
						}
						catch(Exception e){
							sendMessage(e.getMessage() , telegramUser.getChatId());
						}
					}
					// Edit Task Command
					else if(messageTextFromTelegram.equals(BotMessages.EDIT_TASK_COMMAND_MESSAGE.getMessage())){
						sendMessage("Edit Task", telegramUser.getChatId());
					}
					// Delete Task Command
					else if(messageTextFromTelegram.equals(BotMessages.DELETE_TASK_MESSAGE.getMessage())){
						// Mesasge header
						sendMessage(BotMessages.DELETE_TASK_COMMAND_MESSAGE.getMessage(), telegramUser.getChatId());
						// Send to new case
						caseNumber = 3;
					}
					// Create Task Command
					else if(messageTextFromTelegram.equals(BotMessages.CREATE_TASK_COMMAND_MESSAGE.getMessage())){
						// Option Message
						sendMessage(BotMessages.CREATE_TASK_MESSAGE.getMessage(), telegramUser.getChatId());
						// Format message
						sendMessage(BotMessages.CREATE_TASK_FORMAT.getMessage(), telegramUser.getChatId());
						
						// Sprint Info
						sendMessage("\nSprint List", telegramUser.getChatId());
						for(int i = 0; i < sprintList.size(); i++){
							sendMessage(sprintController.printSprintList(sprintList.get(i)), telegramUser.getChatId());
						}
						// Task Status Info
						sendMessage("\nTask Status List", telegramUser.getChatId());
						String info = "";
						for(int i = 0; i < taskStatusList.size(); i++){
							info += taskStatusController.printTaskStatusList(taskStatusList.get(i));
						}
						sendMessage(info, telegramUser.getChatId());
						caseNumber = 4;
					}
					// Manager Button
					// Create Sprint
					else if(messageTextFromTelegram.equals(BotMessages.CREATE_SPRINT_COMMAND_MESSAGE.getMessage())){
						// Home Message
						sendMessage(BotMessages.CREATE_SPRINT_MESSAGE.getMessage(), telegramUser.getChatId());
						// Format Message
						sendMessage(BotMessages.CREATE_SPRINT_FORMAT.getMessage(), telegramUser.getChatId());
						// Project List
						for(int i = 0; i < projectList.size(); i++){
							sendMessage(projectController.printProjectList(projectList.get(i)), telegramUser.getChatId());
						}
						caseNumber = 5;
					}
					// Create Project
					else if(messageTextFromTelegram.equals(BotMessages.CREATE_PROJECT_COMMAND_MESSAGE.getMessage())){
						// Home Message
						sendMessage(BotMessages.CREATE_PROJECT_MESSAGE.getMessage(), telegramUser.getChatId());
						// Format Message
						sendMessage(BotMessages.CREATE_PROJECT_FORMAT.getMessage(), telegramUser.getChatId());
						caseNumber = 6;
					}
					// Show Project 
					else if(messageTextFromTelegram.equals(BotMessages.SHOW_PROJECT_COMMAND_MESSAGE.getMessage())){
						sendMessage("Show Project", telegramUser.getChatId());
					}
					else if(messageTextFromTelegram.equals(BotMessages.LOG_OUT_COMMAND_MESSAGE.getMessage())){
						caseNumber = 0;
						// Log Out Message
						sendMessage(BotMessages.LOG_OUT_MESSAGE.getMessage(), telegramUser.getChatId());
						sendMessage("Use " + BotCommands.START_COMMAND.getCommand() + " to log in", telegramUser.getChatId());
					}
					break;
				// Delete Task
				case 3:
					String taskName = messageTextFromTelegram;
					Long taskId = null;
					for(int i = 0; i < taskList.size(); i++){
						if(taskList.get(i).getName().equals(taskName)){
							taskId = taskList.get(i).getID();
						}
					}
					ResponseEntity<String> deleteTaskResponse = taskController.deleteTask(telegramUser.getID(), taskName, taskId);
					sendMessage(deleteTaskResponse.getBody(), telegramUser.getChatId());
					caseNumber = 2;
					break;
				// Create Task
				case 4:
					// Create task case
					Task newTask = new Task();
					TaskUpdate newTaskUpdate = new TaskUpdate();
					SprintUpdate newSprintUpdate = new SprintUpdate();

					try{
						String[] taskData = messageTextFromTelegram.split("\n");
						
						// Set Name
						String name = "";
						for(int i = 1; i <  taskData[0].split(" ").length; i++){
							name += taskData[0].split(" ")[i] + " ";
						}	
						// sendMessage(name, chatId); // BORRAR
						newTask.setName(name);

						// Description
						String description = "";
						for(int i = 1; i < taskData[1].split(" ").length; i++){
							description += taskData[1].split(" ")[i] + " ";
						}		
						// sendMessage(description, chatId); // BORRAR
						newTask.setDescription(description);

						// Estimated Hours, Priority and Telegram User
						newTask.setEstimatedHours(Float.parseFloat(taskData[2].split(" ")[2]));
						newTask.setPriority(Integer.parseInt(taskData[3].split(" ")[2]));
						newTask.setTelegramUser(telegramUser);
						
						// Set Sprint
						String sprintName = "";
						for(int i = 2; i < taskData[4].split(" ").length; i++){
							sprintName += taskData[4].split(" ")[i] + " ";
						}
						sprintName = sprintName.substring(0, sprintName.length() - 1);
						// sendMessage(sprintName, chatId); // BORRAR
						for(int i = 0; i < sprintList.size(); i++){
							if(sprintList.get(i).getName().equals(sprintName)){
								newTask.setSprint(sprintList.get(i));
								break;
							}
							else{
								newTask.setSprint(new Sprint());
							}
						}

						// Set Task Status
						String taskStatusName = "";
						for(int i = 2; i < taskData[5].split(" ").length; i++){
							taskStatusName += taskData[5].split(" ")[i] + " ";
						}
						taskStatusName = taskStatusName.substring(0, taskStatusName.length() - 1);
						// sendMessage(taskStatusName, chatId); // BORRAR
						for(int i = 0; i < taskStatusList.size(); i++){
							if(taskStatusList.get(i).getName().equals(taskStatusName)){
								newTask.setTaskStatus(taskStatusList.get(i));
								break;
							}
							else{
								newTask.setTaskStatus(new TaskStatus());
							}
						}

						// Set Update Type for TASK UPDATE TABLE && Sprint Update for SPRINT UPDATE TABLE
						for(int i = 0; i < updateTypeList.size(); i++){
							if(updateTypeList.get(i).getName().equals("Creation")){
								Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
								// Task Update
								newTaskUpdate.setTimeStamp(timeStamp);
								newTaskUpdate.setUpdateType(updateTypeList.get(i));
								newTaskUpdate.setTask(newTask);
								newTaskUpdate.setTelegramUser(telegramUser);
								// Sprint Update
								newSprintUpdate.setTimeStamp(timeStamp);
								newSprintUpdate.setUpdateType(updateTypeList.get(i));
								newSprintUpdate.setSprint(newTask.getSprint());
								newSprintUpdate.setTelegramUser(telegramUser);
								break;
							}
						}

						// sendMessage(taskController.printTask(newTask), telegramUser.getChatId()); // BORRAR
						// sendMessage(taskUpdateController.printTaskUpdate(newTaskUpdate), telegramUser.getChatId());
						// sendMessage(sprintUpdateController.printSptintUpdate(newSprintUpdate), telegramUser.getChatId());

						// Post Task to Data Base
						ResponseEntity<String> taskResponse = taskController.createTask(newTask);
						sendMessage(taskResponse.getBody(), telegramUser.getChatId());
						
						// // Post Task Update to Data Base
						taskUpdateController.createTaskUpdate(newTaskUpdate);
						// ResponseEntity<String> taskUpdateResponse = taskUpdateController.createTaskUpdate(newTaskUpdate);
						// sendMessage(taskUpdateResponse.getBody(), telegramUser.getChatId());
						
						// // Post Sprint Update to Data Base
						sprintUpdateController.createNewSprintUpdate(newSprintUpdate);
						// ResponseEntity<String> sprintUpdateResponse = sprintUpdateController.createNewSprintUpdate(newSprintUpdate);
						// sendMessage(sprintUpdateResponse.getBody(), chatId);
					}
					catch(Exception e){
						sendMessage(e.toString(), telegramUser.getChatId());
					}
					caseNumber = 2;
					break;
				// Create Sprint
				case 5:
					// New sprint instance
					Sprint newSprint = new Sprint();
					// String for project name
					String projectName = "";
					// Split the message into a array
					String[] userResponser = messageTextFromTelegram.split("\n");
					
					// Set Name, Description to the project
					//newSprint.setName(userResponser[0].substring(5, userResponser[0].length()));
					sendMessage(userResponser[0].substring(6, userResponser[0].length()), telegramUser.getChatId());
					
					//newSprint.setDescription(userResponser[1].substring(12, userResponser[1].length()));
					sendMessage(userResponser[1].substring(13, userResponser[1].length()), telegramUser.getChatId());

					// Formatter for Date
					LocalDate dateStart = LocalDate.parse(userResponser[2].substring(25, userResponser[2].length()));
					OffsetDateTime dateTimeStart =  dateStart.atStartOfDay().atOffset(ZoneOffset.UTC);
					sendMessage("start " + dateTimeStart, telegramUser.getChatId());
					

					LocalDate dateEnd = LocalDate.parse(userResponser[3].substring(23, userResponser[3].length()));
					OffsetDateTime dateTimeEnd = dateEnd.atStartOfDay().atOffset(ZoneOffset.UTC);
					sendMessage("end " + dateTimeEnd, telegramUser.getChatId());
					//newSprint.setStartDate(dateTime);

					projectName = userResponser[4].substring(13, userResponser[4].length());
					sendMessage(projectName, telegramUser.getChatId());
					// for(int i = 0; i < projectList.size(); i++){
					// 	if(projectList.get(i).getName().equals(projectName)){
					// 		newSprint.setProject(projectList.get(i));
					// 	}
					// }
					caseNumber = 2;
					break;
				// Create Project
				case 6:
					// New project instance
					Project newProject = new Project();
					// Split the message into a array
					String[] userResponse = messageTextFromTelegram.split("\n");
					// Set values to the project
					newProject.setName(userResponse[0].substring(6, userResponse[0].length()));
					newProject.setDescription(userResponse[1].substring(13, userResponse[1].length()));
					
					// Debug Message BORRAR
					sendMessage(newProject.getName() + " " + newProject.getDescription(), telegramUser.getChatId());

					// ResponseEntity<String> newProjectResponse = projectController.createNewProject(newProject);
					// sendMessage(newProjectResponse.getBody(), telegramUser.getChatId());
					caseNumber = 2;
					break;
				// Log in by default
				default:
					// Set information form db to user related models 
					userTypeList = userTypeController.findAllUserType().getBody();
					teamTypeList = teamTypeController.findAllTeamType().getBody();
					teamList = teamController.findAllTeams().getBody();
					userTeamList = userTeamController.findAllUserTeams().getBody();
					telegramUserList = telegramUserController.findAllTelegramUsers().getBody();
					
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

							int chatIdCompare = -1;
							for(int i = 0; i < telegramUserList.size(); i++){
								// Compare Chat id from Users in the Db
								if(telegramUserList != null) chatIdCompare = Long.compare(telegramUserList.get(i).getChatId(), chatId);
								
								// If the values are equal
								if(i == telegramUserList.size()-1 && chatIdCompare != 0){
									sendMessage(BotMessages.LOG_IN_MESSAGE.getMessage(), chatId);	
								}
								else if(chatIdCompare == 0){
									// You have successfully logged in!!
									sendMessage(BotMessages.LOG_IN_SUCCESS.getMessage(), chatId);
									// Set Telegram User Information
									telegramUser = telegramUserList.get(i);
									// Case Number to acces developer or manager methods
									caseNumber++;
									// Continue Message /continue
									sendMessage(BotMessages.CONTINUE_MESSAGE.getMessage(), telegramUser.getChatId());
									break;
								}
							}		
						}
						catch(TelegramApiException e){
							// Log In fail
							sendMessage(BotMessages.LOG_IN_FAIL.getMessage() + e.toString(), chatId);
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
									// Continue Message /continue
									sendMessage(BotMessages.CONTINUE_MESSAGE.getMessage(), telegramUser.getChatId());
									break;
								}
							}
						}					
						catch(TelegramApiException e){
							// Log in fail message
							sendMessage(BotMessages.LOG_IN_FAIL.getMessage() + e.toString(), chatId);
							logger.error(e.getLocalizedMessage(), e);
						}				
					}
					else if(messageTextFromTelegram.equals(BotMessages.LOG_OUT_COMMAND_MESSAGE.getMessage())){
						caseNumber = 0;
						// Log Out Message
						sendMessage(BotMessages.LOG_OUT_MESSAGE.getMessage(), telegramUser.getChatId());
						sendMessage("Use " + BotCommands.START_COMMAND.getCommand() + " to log in", telegramUser.getChatId());
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