package com.springboot.MyTodoList.util;

public enum BotCommands {

	// Todo list commands (default)
	START_COMMAND("/start"),
	HIDE_COMMAND("/hide"), 
	CANCEL_COMMAND("/cancel"),
	TODO_LIST("/todolist"),
	ADD_ITEM("/additem"),
	RESPONSE_COMMAND("/response");

	private String command;

	BotCommands(String enumCommand) {
		this.command = enumCommand;
	}

	public String getCommand() {
		return command;
	}
}
