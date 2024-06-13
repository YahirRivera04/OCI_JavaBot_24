package com.springboot.MyTodoList.controller;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import com.springboot.MyTodoList.model.Conversation;
import com.springboot.MyTodoList.model.Project;
import com.springboot.MyTodoList.model.Sprint;
import com.springboot.MyTodoList.model.SprintUpdate;
import com.springboot.MyTodoList.model.Task;
import com.springboot.MyTodoList.model.TaskStatus;
import com.springboot.MyTodoList.model.TaskUpdate;
import com.springboot.MyTodoList.model.TelegramUser;
import com.springboot.MyTodoList.model.UpdateType;
import com.springboot.MyTodoList.service.ConversationService;
import com.springboot.MyTodoList.service.ProjectService;
import com.springboot.MyTodoList.service.SprintService;
import com.springboot.MyTodoList.service.SprintUpdateService;
import com.springboot.MyTodoList.service.TaskService;
import com.springboot.MyTodoList.service.TaskStatusService;
import com.springboot.MyTodoList.service.TaskUpdateService;
import com.springboot.MyTodoList.service.TelegramUserService;
import com.springboot.MyTodoList.service.UpdateTypeService;

import com.springboot.MyTodoList.util.BotCommands;
import com.springboot.MyTodoList.util.BotLabels;
import com.springboot.MyTodoList.util.BotMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ToDoItemBotController extends TelegramLongPollingBot {

	// Logger information to print in pods
	private static final Logger logger = LoggerFactory.getLogger(ToDoItemBotController.class);
	private TelegramUserService telegramUserService;
	private TaskService taskService;
	private SprintService sprintService;
	private TaskStatusService taskStatusService;
	private UpdateTypeService updateTypeService;
	private ProjectService projectService;
	private TaskUpdateService taskUpdateService;
	private SprintUpdateService sprintUpdateService;
	private ConversationService conversationService;
	private String botName;

	public ToDoItemBotController(String botToken, String botName, TelegramUserService telegramUserService, 
	TaskService taskService, SprintService sprintService, TaskStatusService taskStatusService,UpdateTypeService updateTypeService, 
	ProjectService projectService, TaskUpdateService taskUpdateService, SprintUpdateService sprintUpdateService, ConversationService conversationService) {
		super(botToken);
		logger.info("Bot Token: " + botToken);
		logger.info("Bot name: " + botName);
		this.telegramUserService = telegramUserService;
		this.taskService = taskService;
		this.sprintService = sprintService;
		this.taskStatusService = taskStatusService;
		this.updateTypeService = updateTypeService;
		this.projectService = projectService;
		this.taskUpdateService = taskUpdateService;
		this.sprintUpdateService = sprintUpdateService;
		this.conversationService = conversationService;
		this.botName = botName;
	}

	// New List of Tasks
	List<Task> taskList = List.of(new Task());
	// New List of Sprints
	List<Sprint> sprintList = List.of(new Sprint());
	// New List of Task Status
	List<TaskStatus> taskStatusList = List.of(new TaskStatus());
	// New List of Update Type
	List<UpdateType> updateTypeList = List.of(new UpdateType());
	// New List of Projects
	List<Project> projectList = List.of(new Project());
	// New Telegram Users map
	HashMap<Long, TelegramUser> telegramUserList = new HashMap<>();
	// Auxiliar variable
	int caseNumber = -1;
	// Conversation Object
	Conversation conversation = new Conversation();

	
	@Override
	public void onUpdateReceived (Update update){
		if(update.hasMessage() && update.getMessage().hasText()){
			String messageTextFromTelegram = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
			conversation = conversationService.pushConversationStart();
			handleIncomingMessage(messageTextFromTelegram, chatId, caseNumber, conversation);
		}
	}
	// First case menu
	private void handleIncomingMessage(String messageTextFromTelegram, Long chatId,int caseNumber, Conversation conversation){
		TelegramUser newTelegramUser = new TelegramUser();
		try{
			// sendMessage("Number " + caseNumber, chatId);
			switch (caseNumber) {
				case 0: // /login:TelegramUser
					newTelegramUser = handleLoginCommand(messageTextFromTelegram, chatId);
					telegramUserList.put(newTelegramUser.getChatId(), newTelegramUser);
					
					break;
				case 1: // /continue
					handleContinueCommand(telegramUserList.get(chatId), chatId, messageTextFromTelegram);
					break;
				case 2: // Dev Options
					if(telegramUserList.get(chatId).getUserType().getName().equals("Developer")){
						handelDevOptions(telegramUserList.get(chatId), messageTextFromTelegram);
					}
					else{
						// Manager Options
						caseNumber = 3;
					}
					break;
				case 3: // Manager Options
					if(telegramUserList.get(chatId).getUserType().getName().equals("Manager")){
						handelManagerOptions(telegramUserList.get(chatId), messageTextFromTelegram);
					}
					else{
						// Dev Options
						caseNumber = 2;
					}
					break;
				case 4: // Edit Task
					editTask(messageTextFromTelegram, telegramUserList.get(chatId));
					break;
				case 5: // Delete Task
					deleteTask(messageTextFromTelegram, telegramUserList.get(chatId));
					break;
				case 6: // Create Task
					createTask(messageTextFromTelegram, telegramUserList.get(chatId));
					break;
				case 7: // Create Sprint
					createSprint(messageTextFromTelegram, telegramUserList.get(chatId));
					break;
				case 8: // Create Project
					createProject(messageTextFromTelegram, telegramUserList.get(chatId));
					break;
				case 9: // Log Out
					sendMessage(BotMessages.LOG_OUT_MESSAGE.getMessage(), telegramUserList.get(chatId).getChatId());
					sendMessage("Use " + BotCommands.START_COMMAND.getCommand() + " to log in", telegramUserList.get(chatId).getChatId());
					// Set data to default
					conversationService.pushConversationEnd(conversation);
					telegramUserList.clear();
					taskList.clear();
					taskStatusList.clear();
					sprintList.clear();
					projectList.clear();
					updateTypeList.clear();
					caseNumber = -1;
					break;
				default: // /start
					newTelegramUser = handleStartCommand(chatId);
					telegramUserList.put(newTelegramUser.getChatId(), newTelegramUser);
					break;
			}
		}
		catch(TelegramApiException e){
			logger.error("Fail handeling message {}", e.getMessage());
		}
	}
	// Start
	private TelegramUser handleStartCommand(Long chatId) throws TelegramApiException{
		try{

			// Needed Variables and Objects
			TelegramUser telegramUser = new TelegramUser();
			int chatIdCompare = -1;
			
			// Welcome Message
			sendMessage(BotMessages.WELCOME_MESSAGE.getMessage(), chatId);
			// If the user is found in bb by ChatId
			if(telegramUserService.existsByChatId(chatId)){				
				// Get All Telegram Users and retrieve the actual user
				for(TelegramUser users : telegramUserService.findAllTelegramUsers()){
					// Compare between Chat Id
					if(users.getChatId() != null) chatIdCompare = Long.compare(users.getChatId(), chatId);
					// Set User
					if(chatIdCompare == 0){
						telegramUser = users;
						// Log In Success
						sendMessage(BotMessages.LOG_IN_SUCCESS.getMessage(), telegramUser.getChatId());
						// Continue Message
						sendMessage(BotMessages.CONTINUE_MESSAGE.getMessage(), telegramUser.getChatId());
						break;
					}
				}
				caseNumber = 1;
				return telegramUser;
			}
			else {
				// Log in Fail
				sendMessage(BotMessages.LOG_IN_MESSAGE.getMessage(), chatId);
				caseNumber = 0;
				return new TelegramUser();
			}
		}
		catch(TelegramApiException e){
			// If fails, exit the user
			caseNumber = -1;
			// Logger debug info
			sendMessage("Error " + e.getMessage(), chatId);
			logger.error(e.getLocalizedMessage(), e);
			return new TelegramUser();
		}
	}
	
	// Login
	private TelegramUser handleLoginCommand(String messageTextFromTelegram, Long chatId) throws TelegramApiException{
		try{
			// Retrieve Telegram Name from response
			String responseFromUser = messageTextFromTelegram.substring(BotCommands.LOGIN_COMMAND.getCommand().length());
			// Set Telegram User
			TelegramUser telegramUser = telegramUserService.findByTelegramName(responseFromUser);
			
			if(telegramUser != null && telegramUser.getTelegramName().equals(responseFromUser)){
				// Set Chat Id to User
				telegramUser.setChatId(chatId);
				// Post Chat Id on db 
				telegramUserService.updateChatId(telegramUser.getID(), telegramUser.getChatId());
				// Log In Success
				sendMessage(BotMessages.LOG_IN_SUCCESS.getMessage(), telegramUser.getChatId());
				// Continue Message
				sendMessage(BotMessages.CONTINUE_MESSAGE.getMessage(), telegramUser.getChatId());
				caseNumber = 1;
				return telegramUser;
			}
			else {
				// Log In Fail
				sendMessage(BotMessages.LOG_IN_FAIL.getMessage(), chatId);
				caseNumber = 0;
				return new TelegramUser();
			}
		} 
		catch (TelegramApiException e){
			// If fails, exit the user
			caseNumber = -1;
			// Logger message
			sendMessage(messageTextFromTelegram + " " + e.getMessage(), chatId);
			logger.error(e.getLocalizedMessage(), e);
			return new TelegramUser();
		}


	}
	
	// Continue
	private void handleContinueCommand(TelegramUser telegramUser, Long chatId, String messageTextFromTelegram) throws TelegramApiException{
		try{
			if(telegramUser == null){
				// Log in fail
				sendMessage(BotMessages.LOG_IN_MESSAGE.getMessage(), chatId);
			}
			// Logic for developer
			else if("Developer".equals(telegramUser.getUserType().getName())){
				// Create variables necessaries to interact with telegram					
				SendMessage messageToTelegram = new SendMessage();
				messageToTelegram.setChatId(telegramUser.getChatId());

				// Welcome with credentials
				messageToTelegram.setText("Welcome " + telegramUser.getTelegramName() + 
				", you are logged as " + telegramUser.getUserType().getName());
				
				// Keyboard variables needed for buttons
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
				caseNumber = 2;
				try {
					execute(messageToTelegram);
					sendMessage("To continue, please select any option from the buttons.", telegramUser.getChatId());
				} 
				catch (TelegramApiException e) {
					// If fails, exit the user
					caseNumber = -1;
					logger.error(e.getLocalizedMessage(), e);
				}		
			
			}
			// Logic for manager
			else if("Manager".equals(telegramUser.getUserType().getName())){
				
				// Create variables necessaries to interact with telegram					
				SendMessage messageToTelegram = new SendMessage();
				messageToTelegram.setChatId(telegramUser.getChatId());

				// Welcome with credentials
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
					caseNumber = 3;
				} 
				catch (TelegramApiException e) {
					// If fails, exit the user
					caseNumber = -1;
					logger.error(e.getLocalizedMessage(), e);
				}
			}
		}
		catch(TelegramApiException e){
			// If fails, exit the user
			caseNumber = -1;
			// Logger Inforamtion
			sendMessage(messageTextFromTelegram + " " + e.getMessage(), chatId);
			logger.error(e.getLocalizedMessage(), e);
		}
		
	}
	
	// Developer Buttons
	private void handelDevOptions(TelegramUser telegramUser, String messageTextFromTelegram) throws TelegramApiException{
		try{
			// Load Task Information
			getDataBaseInfo(telegramUser);

			// Show Task Command
			if(messageTextFromTelegram.equals(BotMessages.SHOW_TASK_COMMAND_MESSAGE.getMessage())){
				// Message header
				sendMessage(BotMessages.SHOW_TASK_MESSAGE.getMessage(), telegramUser.getChatId());
				// Show all tasks that belongs to the user
				for(Task task : taskList){
					sendMessage(taskService.printTask(task), telegramUser.getChatId());	
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
				for(Sprint sprint : sprintList){
					sendMessage(sprintService.printSprintList(sprint), telegramUser.getChatId());
				}

				// Task Status Info
				sendMessage("\nTask Status List", telegramUser.getChatId());
				String taskStatusInfo = "";
				for(TaskStatus taskStatus : taskStatusList){
					taskStatusInfo += taskStatusService.printTaskStatusList(taskStatus);
				}
				sendMessage(taskStatusInfo, telegramUser.getChatId());

				// // Update Type
				// sendMessage("\nType of Update", telegramUser.getChatId());
				// String updateTypeInfo = "";
				// for(UpdateType updateType : updateTypeList){
				// 	updateTypeInfo += updateTypeService.printUpdateTypeList(updateType);
				// }
				// sendMessage(updateTypeInfo, telegramUser.getChatId());
				// Set case to Edit task
				caseNumber = 4;
			}
			// Delete Task Command
			else if(messageTextFromTelegram.equals(BotMessages.DELETE_TASK_MESSAGE.getMessage())){
				// Mesasge header
				sendMessage(BotMessages.DELETE_TASK_COMMAND_MESSAGE.getMessage(), telegramUser.getChatId());
				// Set Case to Delete Task
				caseNumber = 5;
			}
			// Create Task Command
			else if(messageTextFromTelegram.equals(BotMessages.CREATE_TASK_COMMAND_MESSAGE.getMessage())){
				// Option Message
				sendMessage(BotMessages.CREATE_TASK_MESSAGE.getMessage(), telegramUser.getChatId());
				// Format message
				sendMessage(BotMessages.CREATE_TASK_FORMAT.getMessage(), telegramUser.getChatId());
				
				// Sprint Info
				sendMessage("\nSprint List", telegramUser.getChatId());
				for(Sprint sprint : sprintList){
					sendMessage(sprintService.printSprintList(sprint), telegramUser.getChatId());
				}

				// Task Status Info
				sendMessage("\nTask Status List", telegramUser.getChatId());
				String taskStatusInfo = "";
				for(TaskStatus taskStatus : taskStatusList){
					taskStatusInfo += taskStatusService.printTaskStatusList(taskStatus);
				}
				sendMessage(taskStatusInfo, telegramUser.getChatId());
				// Set case to Create Task
				caseNumber = 6;
			}
			// Log Out Command
			else if(messageTextFromTelegram.equals(BotCommands.LOG_OUT_COMMAND.getCommand())){
				caseNumber = 9;
			}
		}
		catch (TelegramApiException e){
			// If fails, exit the user
			caseNumber = -1;
			logger.error(e.getLocalizedMessage(), e);
		}
	}

	// Manager Buttons
	private void handelManagerOptions(TelegramUser telegramUser, String messageTextFromTelegram) throws TelegramApiException{
		try{
			// Load Task Information
			getDataBaseInfo(telegramUser);

			// Create Sprint Command
			if(messageTextFromTelegram.equals(BotMessages.CREATE_SPRINT_COMMAND_MESSAGE.getMessage())){
				// Home Message
				sendMessage(BotMessages.CREATE_SPRINT_MESSAGE.getMessage(), telegramUser.getChatId());
				// Format Message
				sendMessage(BotMessages.CREATE_SPRINT_FORMAT.getMessage(), telegramUser.getChatId());
				// Project List
				for(Project project : projectList){
					sendMessage(projectService.printProjectList(project), telegramUser.getChatId());
				}
				// Case Number
				caseNumber = 7;
			}
			// Create Project Command
			else if(messageTextFromTelegram.equals(BotMessages.CREATE_PROJECT_COMMAND_MESSAGE.getMessage())){
				// Home Message
				sendMessage(BotMessages.CREATE_PROJECT_MESSAGE.getMessage(), telegramUser.getChatId());
				// Format Message
				sendMessage(BotMessages.CREATE_PROJECT_FORMAT.getMessage(), telegramUser.getChatId());
				// Case Number
				caseNumber = 8;
			}
			// Show Project Command
			else if(messageTextFromTelegram.equals(BotMessages.SHOW_PROJECT_COMMAND_MESSAGE.getMessage())){						
				// Header Message
				sendMessage(BotMessages.SHOW_PROJECT_MESSAGE.getMessage(), telegramUser.getChatId());
				for(Project project : projectList){
					sendMessage(projectService.printProjectList(project),telegramUser.getChatId());
				}
			}
			// Log Out Command
			else if(messageTextFromTelegram.equals(BotCommands.LOG_OUT_COMMAND.getCommand())){
				caseNumber = 9;
			}
		}
		catch (TelegramApiException e){
			// If fails, exit the user
			caseNumber = -1;
			logger.error(e.getLocalizedMessage(), e);
		}
	}

	// Edit Task
	private void editTask(String messageTextFromTelegram, TelegramUser telegramUser) throws TelegramApiException{
		try{
			// Get Data from Database
			getDataBaseInfo(telegramUser);
			// Bot Commands content
			String[] botComandData = BotMessages.EDIT_TASK_FORMAT.getMessage().split("\n");
			// Retrieve data from user response
			String[] editTaskData = messageTextFromTelegram.split("\n");
			// New Task Instance
			Task editTask = new Task();
			// New Task Update Instance
			TaskUpdate editTaskUpdate = new TaskUpdate();
			// Auxiliar varibale
			int taskNumber = -1;
			// Type of update
			String updateTypeName = "";

			// Set Id
			for(int i = 0; i < taskList.size(); i++){
				if(taskList.get(i).getID().toString().equals(editTaskData[0].substring(botComandData[0].length()).trim())){
					editTask.setID(taskList.get(i).getID());
					taskNumber = i;
				}
			}

			// Set Name
			String name = editTaskData[1].substring(botComandData[1].length(), editTaskData[1].length()).trim();
			if (!name.isEmpty()){
				updateTypeName = "Name change";
			}
			editTask.setName(Optional.of(name)
			.filter(s -> !s.isEmpty())
			.orElse(taskList.get(taskNumber).getName()));

			// Set Description
			String description = editTaskData[2].substring(botComandData[2].length(), editTaskData[2].length()).trim();
			if(!description.isEmpty()){
				updateTypeName = "Description change";
			}
			editTask.setDescription(Optional.of(description)
			.filter(s -> !s.isEmpty())
			.orElse(taskList.get(taskNumber).getDescription()));

			// Estimated Hours
			String estimatedHourData = editTaskData[3].substring(botComandData[3].length(), editTaskData[3].length()).trim();
			if (estimatedHourData.isEmpty()) {
				estimatedHourData = String.valueOf(taskList.get(taskNumber).getEstimatedHours());
			}
			else{
				updateTypeName = "Estimated hours change";
			}
			editTask.setEstimatedHours(Float.parseFloat(estimatedHourData));


			// Set Priority
			String priorityData = editTaskData[4].substring(botComandData[4].length(), editTaskData[4].length()).trim();
			if(priorityData.isEmpty()){
				priorityData = String.valueOf(taskList.get(taskNumber).getPriority());
			}
			else{
				updateTypeName = "Priority change";
			}
			editTask.setPriority(Integer.parseInt(priorityData));

			
			// Set Telegram User
			editTask.setTelegramUser(telegramUser);

			// Set Sprint Id by name
			String sprintName = editTaskData[5].substring(botComandData[5].length(), editTaskData[5].length()).trim();
			if(sprintName.isEmpty()){
				sprintName = String.valueOf(taskList.get(taskNumber).getSprint().getName());
			}
			else{
				updateTypeName = "Sprint change";
			}
			for(int i = 0; i < sprintList.size(); i++){
				if(sprintList.get(i).getName().equals(sprintName)){
					editTask.setSprint(sprintList.get(i));
					break;
				}
			}

			// Set Task Status
			String taskStatusName = editTaskData[6].substring(botComandData[6].length(), editTaskData[6].length()).trim();
			if(taskStatusName.isEmpty()){
				editTask.setTaskStatus(taskList.get(taskNumber).getTaskStatus());
				//sendMessage(editTask.getTaskStatus().getName(), telegramUser.getChatId()); // BORRAR
			}
			else{
				updateTypeName = "Status change";
				for(int i = 0; i < taskStatusList.size(); i++){
					if(taskStatusList.get(i).getName().equals(taskStatusName)){
						editTask.setTaskStatus(taskStatusList.get(i));
						break;
					}
				}
			}

			// Set Task Update with Update Type
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
			String taskResponse = taskService.createTask(editTask);
			sendMessage(taskResponse, telegramUser.getChatId());
			// Update Task Update to Data Base 
			/*String taskUpdateResponse  = */taskUpdateService.createNewTaskUpdate(editTaskUpdate);
			//sendMessage(taskUpdateResponse, telegramUser.getChatId());
			
			// Case Number Dev Options
			caseNumber = 2;
		}
		catch(TelegramApiException e){
			// If fails, exit the user
			caseNumber = -1;
			// Logger info
			sendMessage(messageTextFromTelegram + " " + e.getMessage(), telegramUser.getChatId());
			logger.error(e.getLocalizedMessage(), e);
		}
	}

	// Delete Task 
	private void deleteTask(String messageTextFromTelegram, TelegramUser telegramUser) throws TelegramApiException{
		try {
			// Local update task obect to log
			TaskUpdate deleteTaskUpdate = new TaskUpdate(); 
			// Local task object
			Task task = new Task();
			// Message from user
			String taskName = messageTextFromTelegram.trim();

			// Identify the task in the List
			for(Task slectedTask : taskList){
				if(slectedTask.getName().equals(taskName)){
					task = slectedTask;
					break;
				}
			}

			// Set Update Type
			for(int i = 0; i < updateTypeList.size(); i++){
				if(updateTypeList.get(i).getName().equals("Deletion")){
					Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
					// Task Update
					deleteTaskUpdate.setTimeStamp(timeStamp);
					deleteTaskUpdate.setUpdateType(updateTypeList.get(i));
					deleteTaskUpdate.setTask(task);
					deleteTaskUpdate.setTelegramUser(telegramUser);
					break;
				}
			}

			// Method to delete from db
			String deleteTaskResponse = taskService.deleteTask(telegramUser.getID(), taskName, task.getID());
			sendMessage(deleteTaskResponse, telegramUser.getChatId());
			// Log to TASKUPDATE table
			taskUpdateService.createNewTaskUpdate(deleteTaskUpdate);
			// Case Number Dev Options
			caseNumber = 2;
		}
		catch(Exception e){
			// If fails, exit the user
			caseNumber = -1;
			// Logger info
			sendMessage(messageTextFromTelegram + " " + e.getMessage(), telegramUser.getChatId());
			logger.error(e.getLocalizedMessage(), e);
		}
	}

	// Create Task
	private void createTask(String messageTextFromTelegram, TelegramUser telegramUser) throws TelegramApiException{
		try{
			Task newTask = new Task();
			TaskUpdate newTaskUpdate = new TaskUpdate();
			String[] formatData = BotMessages.CREATE_TASK_FORMAT.getMessage().split("\n");
			String[] taskData = messageTextFromTelegram.split("\n");
			
			// Set Name:
			newTask.setName(taskData[0].substring(formatData[0].length(), taskData[0].length()).trim());
			
			// Description
			newTask.setDescription(taskData[1].substring(formatData[1].length(), taskData[1].length()).trim());

			// Estimated Hours, Priority and Telegram User
			newTask.setEstimatedHours(Float.parseFloat(taskData[2].substring(formatData[2].length(), taskData[2].length()).trim()));
			newTask.setPriority(Integer.parseInt(taskData[3].substring(formatData[3].length(), taskData[3].length()).trim()));
			newTask.setTelegramUser(telegramUser);

			// Set Sprint
			String sprintName = taskData[4].substring(formatData[4].length(), taskData[4].length()).trim();
			//sendMessage("Sprint name " + sprintName, telegramUser.getChatId());

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
			String taskStatusName = taskData[5].substring(formatData[5].length(), taskData[5].length()).trim();
			//sendMessage("Task Status " + taskStatusName, telegramUser.getChatId());

			for(int i = 0; i < taskStatusList.size(); i++){
				if(taskStatusList.get(i).getName().equals(taskStatusName)){
					newTask.setTaskStatus(taskStatusList.get(i));
					break;
				}
				else{
					newTask.setTaskStatus(new TaskStatus());
				}
			}

			// Set Update Type
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
			String taskResponse = taskService.createTask(newTask);
			sendMessage(taskResponse, telegramUser.getChatId());
			// Post Task Update to Data Base
			taskUpdateService.createNewTaskUpdate(newTaskUpdate);
			
			// Case Number Dev Options
			caseNumber = 2;
		}
		catch(Exception e){
			// If fails, exit the user
			caseNumber = -1;
			// Logger info
			sendMessage("Error: " + e.getMessage(), telegramUser.getChatId());
			logger.error(e.getLocalizedMessage(), e);
		}
	}
	
	// Create Sprint
	private void createSprint(String messageTextFromTelegram, TelegramUser telegramUser) throws TelegramApiException{
		try{
			// New sprint instance
			Sprint newSprint = new Sprint();
			// New updateSprint instance 
			SprintUpdate newSprintUpdate = new SprintUpdate();
			// String for project name
			String projectName = "";

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
			String sprintResponse = sprintService.createNewSprint(newSprint);
			sendMessage(sprintResponse, telegramUser.getChatId());

			// Create Sprint Update in Data Base
			sprintUpdateService.createNewSprintUpdate(newSprintUpdate);
			// Return to Manager Options
			caseNumber = 3;			
		}
		catch (TelegramApiException e){
			// If fails, exit the user
			caseNumber = -1;
			// Logger info
			sendMessage(messageTextFromTelegram + " " + e.getMessage(), telegramUser.getChatId());
			logger.error(e.getLocalizedMessage(), e);
		}
	}

	// Create Project
	private void createProject(String messageTextFromTelegram, TelegramUser telegramUser) throws TelegramApiException{
		try{
			// New project instance
			Project newProject = new Project();
			// Split the message into a array
			String[] newProjectResponse = messageTextFromTelegram.split("\n");
			// Set values to the project
			newProject.setName(newProjectResponse[0].substring(6, newProjectResponse[0].length()).trim());
			newProject.setDescription(newProjectResponse[1].substring(13, newProjectResponse[1].length()).trim());
			// Sedn data to data base
			String projectResponse = projectService.createNewProject(newProject);
			sendMessage(projectResponse, telegramUser.getChatId());
			// Return to Button Case
			caseNumber = 3;
		}
		catch (TelegramApiException e){
			// If fails, exit the user
			caseNumber = -1;
			// Logger info
			sendMessage(messageTextFromTelegram + " " + e.getMessage(), telegramUser.getChatId());
			logger.error(e.getLocalizedMessage(), e);
		}
	}

	@Override
	public String getBotUsername() {		
		return botName;
	}

	// Auxiliar Method to print messages
	private void sendMessage(String message, Long chatID) throws TelegramApiException{
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

	// Auxiliar Method to retrieve information from Data Base
	private void getDataBaseInfo(TelegramUser telegramUser){
		if(telegramUser != null){
			taskList = taskService.findAllTaskByTelegramUserId(telegramUser.getID());	
			sprintList = sprintService.findAllSprints();
			taskStatusList = taskStatusService.findAllTaskStatus();
			updateTypeList = updateTypeService.findAllUpdateTypes();
			projectList = projectService.findAllProjects();
		}
		
	}

}