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
	CREATE_TASK_COMMAND_MESSAGE("Create Task"),
	CREATE_TASK_MESSAGE("CREATE TASK \nPlease follow the next format:"),
	CREATE_TASK_FORMAT("Name: \nDescription: \nEstimated Hours: \nPriority (1-3): \nSprint Name: \nTask Status: "),
	
	// Button Messages task
	EDIT_TASK_COMMAND_MESSAGE("Edit Task"),
	EDIT_TASK_MESSAGE("EDIT TASK \nPlease follow the next format (leve in blank no updatable fields) \nId Field no updatable"),
	EDIT_TASK_FORMAT("Id: \nName: \nDescription: \nEstimated Hours: \nPriority (1-3): \nSprint Name: \nTask Status: "),

	DELETE_TASK_MESSAGE("Delete Task"),
	DELETE_TASK_COMMAND_MESSAGE("DELETE TASK \nPlease follow the next format"),
	DELETE_TASK_FORMAT("Id: \nName:"),


	CREATE_SPRINT_COMMAND_MESSAGE("Create Sprint"),
	CREATE_SPRINT_MESSAGE("CREATE SPRINT \nPlease follow the next format:"),
	CREATE_SPRINT_FORMAT("Name: \nDescription: \nStart Date('YYYY-MM-DD'): \nEnd Date('YYYY-MM-DD'): \nProject Name: "),

	CREATE_PROJECT_COMMAND_MESSAGE("Create Project"),
	CREATE_PROJECT_MESSAGE("CREATE PROJECT \nPlease follow the next format:"),
	CREATE_PROJECT_FORMAT("Name: \nDescription: "),
	
	SHOW_PROJECT_COMMAND_MESSAGE("Show Project"),
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
