package com.springboot.MyTodoList.controller;
import org.apache.tomcat.jni.User;
import org.aspectj.weaver.ast.And;
import org.glassfish.hk2.utilities.RethrowErrorService;
import org.mockito.internal.matchers.Null;
import org.springframework.http.ResponseEntity;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.springboot.MyTodoList.model.Project;
import com.springboot.MyTodoList.model.Sprint;
import com.springboot.MyTodoList.model.Task;
import com.springboot.MyTodoList.model.TaskStatus;
import com.springboot.MyTodoList.model.TaskUpdate;
import com.springboot.MyTodoList.model.Team;
import com.springboot.MyTodoList.model.TeamType;
import com.springboot.MyTodoList.model.TelegramUser;
import com.springboot.MyTodoList.model.UpdateType;
import com.springboot.MyTodoList.model.UserTeam;
import com.springboot.MyTodoList.model.UserType;
import com.springboot.MyTodoList.service.ProjectService;
import com.springboot.MyTodoList.service.SprintService;
import com.springboot.MyTodoList.service.TaskService;
import com.springboot.MyTodoList.service.TaskStatusService;
import com.springboot.MyTodoList.service.TaskUpdateService;
import com.springboot.MyTodoList.service.TelegramUserService;
import com.springboot.MyTodoList.service.UpdateTypeService;
import com.springboot.MyTodoList.service.UserTypeService;

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

import io.swagger.models.Response;
import com.fasterxml.jackson.datatype.jdk8.LongStreamSerializer;


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
	private String botName;

	public ToDoItemBotController(String botToken, String botName, TelegramUserService telegramUserService, 
	TaskService taskService, SprintService sprintService, TaskStatusService taskStatusService,UpdateTypeService updateTypeService, 
	ProjectService projectService, TaskUpdateService taskUpdateService) {
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
	// Telegram User
	TelegramUser telegramUser = new TelegramUser();
	
	@Override
	public void onUpdateReceived (Update update){
		if(update.hasMessage() && update.getMessage().hasText()){
			String messageTextFromTelegram = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            handleIncomingMessage(messageTextFromTelegram, chatId);
		}
	}
	// First case menu
	private void handleIncomingMessage(String messageTextFromTelegram, Long chatId){

		try{
			switch (messageTextFromTelegram) {
				case "/start": // BotCommands.START_COMMAND
					telegramUser = handleStartCommand(chatId);
					break;
				case "/continue": // BotCommands.CONTINUE_COMMAND
					handleContinueCommand(telegramUser, chatId, messageTextFromTelegram);
					break;
				case "Log Out":
					sendMessage(BotMessages.LOG_OUT_MESSAGE.getMessage(), telegramUser.getChatId());
					sendMessage("Use " + BotCommands.START_COMMAND.getCommand() + " to log in", telegramUser.getChatId());
					break;
				case "Edit Task": // BotCommands.EDIT_TASK
					// Edit task function
					editTask(messageTextFromTelegram, telegramUser);
					break;
				case "Delete Task":
					deleteTask(messageTextFromTelegram, telegramUser);
					break;
				case "Create Task":
					createTask(messageTextFromTelegram, telegramUser);
					break;
				default: // BotCommands.LOGIN_COMMAND
					if (messageTextFromTelegram.startsWith(BotCommands.LOGIN_COMMAND.getCommand())) {
						telegramUser = handleLoginCommand(messageTextFromTelegram, chatId);
					}
					break;
			}
		}
		catch(TelegramApiException e){
			logger.error("Fail handeling message {}", e.getMessage());
		}
	}
	// Start
	private TelegramUser handleStartCommand(Long chatId) throws TelegramApiException{
		// Needed Variables and Objects
		TelegramUser telegramUser = new TelegramUser();
		int chatIdCompare = -1;

		try{
			// Welcome Message
			sendMessage(BotMessages.WELCOME_MESSAGE.getMessage(), chatId);
			// If the user is found in bb by ChatId
			if(telegramUserService.existsByChatId(chatId)){
				// Get All Telegram Users and retrieve the actual user
				for(TelegramUser users : telegramUserService.findAllTelegramUsers()){
					// Compare between Chat Id
					if(users != null) chatIdCompare = Long.compare(users.getChatId(), chatId);
					// Set User
					if(chatIdCompare == 0){
						telegramUser = users;
						// Log In Success
						sendMessage(BotMessages.LOG_IN_SUCCESS.getMessage(), chatId);
						// Continue Message
						sendMessage(BotMessages.CONTINUE_MESSAGE.getMessage(), chatId);
						break;
					}
				}
				return telegramUser;
			}
			else {
				// Log in Fail
				sendMessage(BotMessages.LOG_IN_MESSAGE.getMessage(), chatId);
				return new TelegramUser();
			}
		}
		catch(TelegramApiException e){
			// Logger debug info
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
				sendMessage(BotMessages.LOG_IN_SUCCESS.getMessage(), chatId);
				// Continue Message
				sendMessage(BotMessages.CONTINUE_MESSAGE.getMessage(), chatId);
				return telegramUser;
			}
			else {
				// Log In Fail
				sendMessage(BotMessages.LOG_IN_FAIL.getMessage(), chatId);
				return new TelegramUser();
			}
		} 
		catch (TelegramApiException e){
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

				try {
					execute(messageToTelegram);
					sendMessage("To continue, please select any option from the buttons.", telegramUser.getChatId());
					handelDevOptions(telegramUser, messageTextFromTelegram);
				} 
				catch (TelegramApiException e) {
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
					handelmanagerOptions(telegramUser, messageTextFromTelegram);
				} 
				catch (TelegramApiException e) {
					logger.error(e.getLocalizedMessage(), e);
				}
			}
			else{
				sendMessage(telegramUser.getUserType().getName(), chatId);
				sendMessage(messageTextFromTelegram, chatId);
			}
		}
		catch(TelegramApiException e){
			logger.error(e.getLocalizedMessage(), e);
		}
		
	}
	// Developer Buttons
	private void handelDevOptions(TelegramUser telegramUser, String messageTextFromTelegram) throws TelegramApiException{
		try{
			// Load Task Information
			getDataBaseInfo(telegramUser);

			//Show Task Command
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

				// Update Type
				sendMessage("\nType of Update", telegramUser.getChatId());
				String updateTypeInfo = "";
				for(UpdateType updateType : updateTypeList){
					updateTypeInfo += updateTypeService.printUpdateTypeList(updateType);
				}
				sendMessage(updateTypeInfo, telegramUser.getChatId());

			}
			// Delete Task Command
			else if(messageTextFromTelegram.equals(BotMessages.DELETE_TASK_MESSAGE.getMessage())){
				// Mesasge header
				sendMessage(BotMessages.DELETE_TASK_COMMAND_MESSAGE.getMessage(), telegramUser.getChatId());
				// Llamar a Función cn3
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

				// Llamar a fución
			}
		
		}
		catch (TelegramApiException e){
			logger.error(e.getLocalizedMessage(), e);
		}
	}

	// Manager Buttons
	private void handelmanagerOptions(TelegramUser telegramUser, String messageTextFromTelegram) throws TelegramApiException{
		// try{

		// }
		// catch (TelegramApiException e){
		// 	logger.error(e.getLocalizedMessage(), e);
		// }
	}

	// Edit Task
	private void editTask(String messageTextFromTelegram, TelegramUser telegramUser) throws TelegramApiException{
		try{
			// Get Data from Database
			getDataBaseInfo(telegramUser);
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
			String taskUpdateResponse = taskService.createTask(editTask);
			sendMessage(taskUpdateResponse, telegramUser.getChatId());
			// Update Task Update to Data Base 
			taskUpdateService.createNewTaskUpdate(editTaskUpdate);
		}
		catch(TelegramApiException e){
			logger.error(e.getLocalizedMessage(), e);
		}
	}

	// Delete Task 
	private void deleteTask(String messageTextFromTelegram, TelegramUser telegramUser){
		try {
			String taskName = messageTextFromTelegram.trim();
			Long taskId = null;

			for(int i = 0; i < taskList.size(); i++){
				if(taskList.get(i).getName().equals(taskName)){
					taskId = taskList.get(i).getID();
					break;
				}
			}
			String deleteTaskResponse = taskService.deleteTask(telegramUser.getID(), taskName, taskId);
			sendMessage(deleteTaskResponse, telegramUser.getChatId());
		}
		catch(Exception e){
			logger.error(e.getLocalizedMessage(), e);
		}
	}

	// Create Task
	private void createTask(String messageTextFromTelegram, TelegramUser telegramUser){
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
			String taskResponse = taskService.createTask(newTask);
			sendMessage(taskResponse, telegramUser.getChatId());
			
			// Post Task Update to Data Base
			taskUpdateService.createNewTaskUpdate(newTaskUpdate);

		}
		catch(Exception e){
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