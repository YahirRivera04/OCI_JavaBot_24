package com.springboot.MyTodoList.controller;
import com.springboot.MyTodoList.model.Task;
import com.springboot.MyTodoList.model.TaskUpdate;
import com.springboot.MyTodoList.service.TaskUpdateService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class TaskUpdateController {
    @Autowired
    private TaskUpdateService taskUpdateService;

    // ##################### Task Update Controller Metods ##################### //
    
    // Post Task Update
    @GetMapping(value = "/taskupdate/{TaskUpdate}")
    public ResponseEntity<String> createTaskUpdate(@PathVariable TaskUpdate taskUpdate){
        return ResponseEntity.ok(taskUpdateService.createNewTaskUpdate(taskUpdate));
    }

    // ##################### Bot Controller Metods ##################### //
    
    // Post Task Update
    public ResponseEntity<String> postTaskUpdate(TaskUpdate taskUpdate){
        return ResponseEntity.ok(taskUpdateService.createNewTaskUpdate(taskUpdate));
    }

    public String printTaskUpdate(TaskUpdate taskUpdate){
        String info = "Id: " + taskUpdate.getID() + 
        " \nTime Stamp: " + taskUpdate.getTimeStamp() +
        " \nUpdate Type: " + taskUpdate.getUpdateType().getName() +
        " \nTask Name: " + taskUpdate.getTask().getName() +
        " \nTelegram User Name: " + taskUpdate.getTelegramUser().getName();

        return info;
    }

}
