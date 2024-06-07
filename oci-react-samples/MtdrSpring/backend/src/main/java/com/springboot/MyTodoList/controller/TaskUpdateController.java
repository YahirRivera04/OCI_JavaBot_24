package com.springboot.MyTodoList.controller;
import com.springboot.MyTodoList.model.TaskUpdate;
import com.springboot.MyTodoList.service.TaskUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

}
