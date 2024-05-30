package com.springboot.MyTodoList.util;

public enum BotLabels {
	
	START("/start"), 
	HIDE_MAIN_SCREEN("Hide Main Screen"),
	LIST_ALL_ITEMS("List All Items"), 
	ADD_NEW_ITEM("Add New Item"),
	DONE("DONE"),
	UNDO("UNDO"),
	DELETE("DELETE"),
	MY_TODO_LIST("MY TODO LIST"),
	DASH("-"),

	SHOW_TASK("Show Task"),
	EDIT_TASK("Edit Task"),
	DELETE_TASK("Delete Task"),
	CREATE_TASK("Create Task");

	private String label;

	BotLabels(String enumLabel) {
		this.label = enumLabel;
	}

	public String getLabel() {
		return label;
	}

}
