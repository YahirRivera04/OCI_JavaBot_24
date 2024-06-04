package com.springboot.MyTodoList.util;

public enum BotLabels {
	
	// Default buttons
	START("/start"), 
	LOGOUT("Log Out"),
	HIDE_MAIN_SCREEN("Hide Main Screen"),
	LIST_ALL_ITEMS("List All Items"), 
	ADD_NEW_ITEM("Add New Item"),
	DONE("DONE"),
	UNDO("UNDO"),
	DELETE("DELETE"),
	MY_TODO_LIST("MY TODO LIST"),
	DASH("-"),

	// Developer buttons
	SHOW_TASK("Show Task"),
	EDIT_TASK("Edit Task"),
	DELETE_TASK("Delete Task"),
	CREATE_TASK("Create Task"),

	// Manager buttons
	SHOW_PROJECT("View Project"),
	SHOW_USERS_IN_PROJECT("Show Users in Project"),
	SHOW_ALL_PROJECTS("Show all Projects"),

	CREATE_PROJECT("Create Project"),
	CREATE_SPRINT("Create Sprint"),

	CANCEL("Cancel");


	private String label;

	BotLabels(String enumLabel) {
		this.label = enumLabel;
	}

	public String getLabel() {
		return label;
	}

}
