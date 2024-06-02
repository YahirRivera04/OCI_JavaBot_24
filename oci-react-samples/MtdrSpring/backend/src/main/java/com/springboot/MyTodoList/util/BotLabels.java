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
	CREATE_PROJECT("Create Project"),
	CREATE_SPRINT("Create Sprint"),

	// Task Buttons
	SHOW_TASK_SPRINT("Show Tasks by Sprint"),
	SHOW_ALL_TASKS("Show All Tasks");


	private String label;

	BotLabels(String enumLabel) {
		this.label = enumLabel;
	}

	public String getLabel() {
		return label;
	}

}
