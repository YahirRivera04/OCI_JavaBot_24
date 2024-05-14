package com.springboot.MyTodoList.util;

public enum BotCommands {

	START_COMMAND("/start"),
	HIDE_COMMAND("/hide"), 
	CANCEL_COMMAND("/cancel"),

	TODO_LIST("/todolist"),
	ADD_ITEM("/additem"),
	READ_ACTIVITY("/activity"),
	WRITE_ACTIVITY("/addactivity"),
	UPDATE_ACTIVITY("/updateactivity");

	private String command;

	BotCommands(String enumCommand) {
		this.command = enumCommand;
	}

	public String getCommand() {
		return command;
	}
}
