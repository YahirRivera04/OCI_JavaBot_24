package com.springboot.MyTodoList.util;

public enum BotMessages {
	
	BOT_REGISTERED_STARTED("Bot registered and started succesfully!"),
	// Log in messages
	WELCOME_MESSAGE("Welcome to your Java Bot, please wait for the authorization..."),
	LOG_IN_MESSAGE("Enter your Telegram Username with format /login:TelegramUsername"),
	LOG_IN_SUCCESS("You have successfully logged in!"),
	LOG_IN_FAIL("Login failed! Please try again!"),
	// Log Out messages
	LOG_OUT_MESSAGE("See you next time"),
	LOG_OUT_COMMAND_MESSAGE("Log Out"),

	// Show tasks
	SHOW_TASK_COMMAND_MESSAGE("Show Task"),
	SHOW_TASK_MESSAGE("SHOW TASKS \nHere are all your tasks: "),

	// Continue 
	CONTINUE_MESSAGE("To continue, please use the command /continue"),	

	// Edit or Create Task
	EDIT_TASK_MESSAGE("EDIT TASK"),
	CREATE_TASK_COMMAND_MESSAGE("Create Task"),
	CREATE_TASK_MESSAGE("CREATE TASK \nPlease follow the next format:"),
	CREATE_TASK_FORMAT("Name: \nDescription: \nEstimated Hours: \nPriority (1-3): \nSprint Name: \nTask Status: "),

	EDIT_TASK_NAME("Name:"),
	EDIT_TASK_DESCRIPTION("Description:"),
	EDIT_TASK_ESTIMATED_HOURS("Estimated Hours:"),
	EDIT_TASK_PRIORITY("Priority:"),
	EDIT_TASK_SPRINT("Sprint Name:"),
	EDIT_TASK_STATUS("Task Status:"),
	EDIT_TASK_UPDATE("Type of Update:"),
	
	// Button Messages task
	EDIT_TASK_COMMAND_MESSAGE("Edit Task"),

	DELETE_TASK_MESSAGE("Delete Task"),
	DELETE_TASK_COMMAND_MESSAGE("DELETE TASK \nPlease send the name of the task to be deleated"),

	CREATE_SPRINT_COMMAND_MESSAGE("Create Sprint"),
	CREATE_SPRINT_MESSAGE("CREATE SPRINT \nPlease follow the next format:"),
	CREATE_SPRINT_FORMAT("Name: \nDescription: \nStart Date('YYYY-MM-DD'): \nEnd Date('YYYY-MM-DD'): \nProject Name: "),

	CREATE_PROJECT_COMMAND_MESSAGE("Create Project"),
	CREATE_PROJECT_MESSAGE("CREATE PROJECT \nPlease follow the next format:"),
	CREATE_PROJECT_FORMAT("Name: \nDescription: "),
	
	SHOW_PROJECT_COMMAND_MESSAGE("Show Project"),
	SHOW_USERS_IN_PROJECT_MESSAGE("Show Users in Project"),
	SHOW_USERS_MESSAGE("SHOW USERS IN PROJECT\nHere are all the Users in the Project"),
	SHOW_ALL_PROJECTS_MESSAGE("Show all Projects"),
	SHOW_PROJECT_MESSAGE("SHOW PROJECT \nHere are all your projects"),

	CANCEL_MESSAGE("Cancel");


	private String message;

	BotMessages(String enumMessage) {
		this.message = enumMessage;
	}

	public String getMessage() {
		return message;
	}

}
