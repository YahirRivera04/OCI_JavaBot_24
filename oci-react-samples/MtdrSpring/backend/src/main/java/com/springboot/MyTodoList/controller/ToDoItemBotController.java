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
import java.util.Optional;

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
			// Get text from telegram message
			String messageTextFromTelegram = update.getMessage().getText();
			// Get the Telegram Chat Id from Telegram
			Long chatId = update.getMessage().getChatId();
			
			switch (caseNumber) {
				// When already logged
				case 1: // Set Buttons case
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
				case 2: // Actions Buttons Menu
					try{
						// Set information form db to task related models 
						projectList = projectController.findAllProjects().getBody();
						sprintList = sprintController.findAllSprints().getBody();
						taskList = taskController.findAllTaskByTelegramUserId(telegramUser.getID()).getBody();	
					}
					catch(Exception e){
						sendMessage(e.getMessage() , telegramUser.getChatId());
					}
					// Developer Buttons
					//Show Task Command
					if(messageTextFromTelegram.equals(BotMessages.SHOW_TASK_COMMAND_MESSAGE.getMessage())){
						// Message header
						sendMessage(BotMessages.SHOW_TASK_MESSAGE.getMessage(),telegramUser.getChatId());
						// Show all tasks that belongs to the user
						for(int i = 0; i < taskList.size(); i++){
							sendMessage(taskController.printTask(taskList.get(i)), telegramUser.getChatId());
						}
					}
					// Edit Task Command
					else if(messageTextFromTelegram.equals(BotMessages.EDIT_TASK_COMMAND_MESSAGE.getMessage())){
						// Header Message
						sendMessage(BotMessages.EDIT_TASK_MESSAGE.getMessage(), telegramUser.getChatId());
						// Format Message
						sendMessage(BotMessages.EDIT_TASK_FORMAT.getMessage(), telegramUser.getChatId());

						// Sprint Info
						sendMessage("\nSprint List", telegramUser.getChatId());
						for(int i = 0; i < sprintList.size(); i++){
							sendMessage(sprintController.printSprintList(sprintList.get(i)), telegramUser.getChatId());
						}
						// Task Status Info
						sendMessage("\nTask Status List", telegramUser.getChatId());
						String taskStatusInfo = "";
						for(int i = 0; i < taskStatusList.size(); i++){
							taskStatusInfo += taskStatusController.printTaskStatusList(taskStatusList.get(i));
						}
						sendMessage(taskStatusInfo, telegramUser.getChatId());

						// Update Type
						sendMessage("\nType of Update", telegramUser.getChatId());
						String updateTypeInfo = "";
						for(int i = 0; i < updateTypeList.size(); i++){
							updateTypeInfo += updateTypeController.printUpdateTypeList(updateTypeList.get(i));
						}
						sendMessage(updateTypeInfo, telegramUser.getChatId());

						// Edit task case
						caseNumber = 7;
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
					// Create Sprint Command
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
					// Create Project Command
					else if(messageTextFromTelegram.equals(BotMessages.CREATE_PROJECT_COMMAND_MESSAGE.getMessage())){
						// Home Message
						sendMessage(BotMessages.CREATE_PROJECT_MESSAGE.getMessage(), telegramUser.getChatId());
						// Format Message
						sendMessage(BotMessages.CREATE_PROJECT_FORMAT.getMessage(), telegramUser.getChatId());
						caseNumber = 6;
					}
					// Show Project Command
					else if(messageTextFromTelegram.equals(BotMessages.SHOW_PROJECT_COMMAND_MESSAGE.getMessage())){						
						// Header Message
						sendMessage(BotMessages.SHOW_PROJECT_MESSAGE.getMessage(), telegramUser.getChatId());
						for(int i = 0; i < projectList.size(); i++){
							sendMessage(projectController.printProjectList(projectList.get(i)), chatId);
						}
					}
					// Log Out Message
					else if(messageTextFromTelegram.equals(BotMessages.LOG_OUT_COMMAND_MESSAGE.getMessage())){
						caseNumber = 0;
						// Log Out Message
						sendMessage(BotMessages.LOG_OUT_MESSAGE.getMessage(), telegramUser.getChatId());
						sendMessage("Use " + BotCommands.START_COMMAND.getCommand() + " to log in", telegramUser.getChatId());
					}
					break;
				case 3: // Delete Task
					String taskName = messageTextFromTelegram.trim();

					sendMessage(taskName, chatId);

					Long taskId = null;
					for(int i = 0; i < taskList.size(); i++){
						if(taskList.get(i).getName().equals(taskName)){
							taskId = taskList.get(i).getID();
							break;
						}
					}
					ResponseEntity<String> deleteTaskResponse = taskController.deleteTask(telegramUser.getID(), taskName, taskId);
					sendMessage(deleteTaskResponse.getBody(), telegramUser.getChatId());
					caseNumber = 2;
					break;
				case 4: // Create Task
					// Create task case
					Task newTask = new Task();
					TaskUpdate newTaskUpdate = new TaskUpdate();

					try{
						String[] taskData = messageTextFromTelegram.split("\n");
						
						// Set Name
						newTask.setName(taskData[0].substring(6, taskData[0].length()).trim());
						// Description
						newTask.setDescription(taskData[1].substring(13, taskData[1].length()).trim());

						// Estimated Hours, Priority and Telegram User
						newTask.setEstimatedHours(Float.parseFloat(taskData[2].substring(16, taskData[2].length()).trim()));
						newTask.setPriority(Integer.parseInt(taskData[3].substring(16, taskData[2].length()).trim()));
						newTask.setTelegramUser(telegramUser);
						
						// Set Sprint
						String sprintName = taskData[4].substring(13, taskData[4].length()).trim();
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
						String taskStatusName = taskData[5].substring(13, taskData[5].length()).trim();
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
								break;
							}
						}

						// Post Task to Data Base
						ResponseEntity<String> taskResponse = taskController.createTask(newTask);
						sendMessage(taskResponse.getBody(), telegramUser.getChatId());
						
						// Post Task Update to Data Base
						taskUpdateController.createTaskUpdate(newTaskUpdate);

					}
					catch(Exception e){
						sendMessage(e.toString(), telegramUser.getChatId());
					}
					caseNumber = 2;
					break;
				case 5:	// Create Sprint
					// New sprint instance
					Sprint newSprint = new Sprint();
					// New updateSprint instance 
					SprintUpdate newSprintUpdate = new SprintUpdate();
					// String for project name
					String projectName = "";

					try{
						// Split the message into a array
						String[] userResponser = messageTextFromTelegram.split("\n");
						
						// Set Name, Description to the project
						newSprint.setName(userResponser[0].substring(6, userResponser[0].length()).trim());
						newSprint.setDescription(userResponser[1].substring(13, userResponser[1].length()).trim());

						// Formatter for Start Date
						LocalDate dateStart = LocalDate.parse(userResponser[2].substring(26, userResponser[2].length()).trim());
						OffsetDateTime dateTimeStart =  dateStart.atStartOfDay().atOffset(ZoneOffset.UTC);
						newSprint.setStartDate(dateTimeStart);
						
						// Formatter for End Date
						LocalDate dateEnd = LocalDate.parse(userResponser[3].substring(24, userResponser[3].length()).trim());
						OffsetDateTime dateTimeEnd = dateEnd.atStartOfDay().atOffset(ZoneOffset.UTC);
						//sendMessage("End " + dateTimeEnd, telegramUser.getChatId());
						newSprint.setEndDate(dateTimeEnd);

						// Set Project
						projectName = userResponser[4].substring(14, userResponser[4].length()).trim();
						sendMessage(projectName, telegramUser.getChatId());
						for(int i = 0; i < projectList.size(); i++){
							if(projectList.get(i).getName().equals(projectName)){
								newSprint.setProject(projectList.get(i));
							}
						}

						// Create Sprint Update
						for(int i = 0; i < updateTypeList.size(); i++){
							if(updateTypeList.get(i).getName().equals("Creation")){
								Timestamp timeStamp = new Timestamp(System.currentTimeMillis()); 
								newSprintUpdate.setTimeStamp(timeStamp);
								newSprintUpdate.setUpdateType(updateTypeList.get(i));
								newSprintUpdate.setSprint(newSprint);
								newSprintUpdate.setTelegramUser(telegramUser);
								break;
							}
						}
						
						// Create Sprint in Data Base
						ResponseEntity<String> sprintResponse = sprintController.createSprint(newSprint);
						sendMessage(sprintResponse.getBody(), telegramUser.getChatId());

						// Create Sprint Update in Data Base
						sprintUpdateController.createNewSprintUpdate(newSprintUpdate);



					}
					catch(Exception e){
						sendMessage(e.toString(), telegramUser.getChatId());
					}
					// Return to Button Case
					caseNumber = 2;
					break;
				case 6: // Create Project
					// New project instance
					Project newProject = new Project();
					// Split the message into a array
					String[] newProjectResponse = messageTextFromTelegram.split("\n");
					// Set values to the project
					newProject.setName(newProjectResponse[0].substring(6, newProjectResponse[0].length()).trim());
					newProject.setDescription(newProjectResponse[1].substring(13, newProjectResponse[1].length()).trim());
					// Sedn data to data base
					ResponseEntity<String> projectResponse = projectController.createNewProject(newProject);
					sendMessage(projectResponse.getBody(), telegramUser.getChatId());
					// Return to Button Case
					caseNumber = 2;
					break;
				case 7: // Edit Task
					// Retrieve data from user response
					String[] editTaskData = messageTextFromTelegram.split("\n");
					// New Task Instance
					Task editTask = new Task();
					// New Task Update Instance
					TaskUpdate editTaskUpdate = new TaskUpdate();
					// Auxiliar varibale
					int taskNumber = -1;
					
					// Set Id
					for(int i = 0; i < taskList.size(); i++){
						if(taskList.get(i).getID().toString().equals(editTaskData[0].substring(3, editTaskData[0].length()).trim())){
							editTask.setID(taskList.get(i).getID());
							taskNumber = i;
						}
					}

					// Set Name
					editTask.setName(Optional.of(editTaskData[1].substring(6, editTaskData[1].length()).trim())
					.filter(s -> !s.isEmpty())
					.orElse(taskList.get(taskNumber).getName()));

					// Set Description
					editTask.setDescription(Optional.of(editTaskData[2].substring(13, editTaskData[2].length()).trim())
					.filter(s -> !s.isEmpty())
					.orElse(taskList.get(taskNumber).getDescription()));

					// Estimated Hours
					String estimatedHourData = editTaskData[3].substring(16, editTaskData[3].length()).trim();
					if (estimatedHourData.isEmpty()) {
						estimatedHourData = String.valueOf(taskList.get(taskNumber).getEstimatedHours());
					}
					editTask.setEstimatedHours(Float.parseFloat(estimatedHourData));

					// Set Priority
					String priorityData = editTaskData[4].substring(16, editTaskData[4].length()).trim();
					if(priorityData.isEmpty()){
						priorityData = String.valueOf(taskList.get(taskNumber).getPriority());
					}
					editTask.setPriority(Integer.parseInt(priorityData));
					
					// Set Telegram User
					editTask.setTelegramUser(telegramUser);

					// Set Sprint Id by name
					String sprintName = editTaskData[5].substring(13, editTaskData[5].length()).trim();
					if(sprintName.isEmpty()){
						sprintName = String.valueOf(taskList.get(taskNumber).getSprint().getName());
					}
					for(int i = 0; i < sprintList.size(); i++){
						if(sprintList.get(i).getName().equals(sprintName)){
							editTask.setSprint(sprintList.get(i));
							break;
						}
					}

					// Set Task Status
					String taskStatusName = editTaskData[6].substring(12, editTaskData[6].length()).trim();
					if(taskStatusName.isEmpty()){
						taskStatusName = String.valueOf(taskList.get(taskNumber).getTaskStatus().getName());
					}
					for(int i = 0; i < taskStatusList.size(); i++){
						if(taskStatusList.get(i).getName().equals(taskStatusName)){
							editTask.setTaskStatus(taskStatusList.get(i));
							break;
						}
					}

					// Set Task Update with Update Type
					String updateTypeName = editTaskData[7].substring(15, editTaskData[7].length()).trim();
					for(int i = 0; i < updateTypeList.size(); i++){
						if(updateTypeList.get(i).getName().equals(updateTypeName)){
							Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
							// Task Update
							editTaskUpdate.setTimeStamp(timeStamp);
							editTaskUpdate.setUpdateType(updateTypeList.get(i));
							editTaskUpdate.setTask(editTask);
							editTaskUpdate.setTelegramUser(telegramUser);
							break;
						}
					}

					// Update Task to Data Base
					ResponseEntity<String> taskUpdateResponse = taskController.createNewTask(editTask);
					sendMessage(taskUpdateResponse.getBody(), chatId);
					// Update Task Update to Data Base 
					taskUpdateController.createTaskUpdate(editTaskUpdate);
					// Return to Button Case
					caseNumber = 2;
					break;
				// Log in by default
				default: // Authenication
					// Set information form db to user related models 
					userTypeList = userTypeController.findAllUserType().getBody();
					teamTypeList = teamTypeController.findAllTeamType().getBody();
					teamList = teamController.findAllTeams().getBody();
					userTeamList = userTeamController.findAllUserTeams().getBody();
					telegramUserList = telegramUserController.findAllTelegramUsers().getBody();
					taskStatusList = taskStatusController.findAllTaskStatus().getBody();
					updateTypeList = updateTypeController.findAllUpdateType().getBody();
					
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
								if(telegramUserList.get(i).getChatId() != null) chatIdCompare = Long.compare(telegramUserList.get(i).getChatId(), chatId);
								
								if(chatIdCompare == 0){
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
							// If the values are equal
							if(chatIdCompare != 0){
								sendMessage(BotMessages.LOG_IN_MESSAGE.getMessage(), chatId);	
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
									telegramUserController.updateChatId(telegramUser.getID(), telegramUser.getChatId());
									//ResponseEntity<String> response = telegramUserController.updateChatId(telegramUser.getID(), telegramUser.getChatId());
									//sendMessage(response.getBody(), telegramUser.getChatId());
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