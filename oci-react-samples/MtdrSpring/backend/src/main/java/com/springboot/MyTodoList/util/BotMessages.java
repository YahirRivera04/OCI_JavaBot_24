package com.springboot.MyTodoList.util;

import org.assertj.core.error.ShouldBeWritable;

public enum BotMessages {
	
	BOT_REGISTERED_STARTED("Bot registered and started succesfully!"),
	// Log in messages
	WELCOME_MESSAGE("Welcome to your Java Bot, please wait for the authorization..."),
	LOG_IN_MESSAGE("Enter your Telegram Username with format /login:TelegramUsername"),
	LOG_IN_SUCCESS("You have successfully logged in!"),
	LOG_IN_FAIL("Login failed! Please try again!"),

	// Show tasks
	SHOW_TASK_MESSAGE("Here are your tasks: \n"),

	// Edit task
	EDIT_TASK_MESSAGE("Edit your task below:"),
	EDIT_TASK_NAME("Name:"),
	EDIT_TASK_DESCRIPTION("Description:"),
	EDIT_TASK_ESTIMATED_HOURS("Estimated Hours:"),
	EDIT_TASK_PRIORITY("Priority:"),
	EDIT_TASK_STATUS("Select the status: \n"),
	TASK_UPDATE("Select the update type: \n"),
	EDIT_TASK_SUCCESS("Task edited successfully!"),

	// Create task
	CREATE_TASK_MESSAGE("Create a new task below:"),
	CREATE_TASK_SUCCESS("Task created successfully!");


	private String message;

	BotMessages(String enumMessage) {
		this.message = enumMessage;
	}

	public String getMessage() {
		return message;
	}

}
