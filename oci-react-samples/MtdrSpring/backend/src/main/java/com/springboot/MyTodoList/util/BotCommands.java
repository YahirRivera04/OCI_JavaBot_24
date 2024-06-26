package com.springboot.MyTodoList.util;

public enum BotCommands {

	// Todo list commands (default)
	START_COMMAND("/start"),
	LOG_OUT_COMMAND("Log Out"),

	// Start commands
	LOGIN_COMMAND("/login:"),
	CONTINUE_COMMAND("/continue"),

	// Developer buttons
	SHOW_TASK_COMMAND("Show Task"),
	EDIT_TASK_COMMAND("Edit Task"),
	DELETE_TASK_COMMAND("Delete Task"),
	CREATE_TASK_COMMAND("Create Task"),

	// Manager buttons
	SHOW_PROJECT_COMMAND("View Project"),
	CREATE_PROJECT_COMMAND("Create Project"),
	CREATE_SPRINT_COMMAND("Create Sprint");

	private String command;

	BotCommands(String enumCommand) {
		this.command = enumCommand;
	}

	public String getCommand() {
		return command;
	}
}
