package com.springboot.MyTodoList.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import com.springboot.MyTodoList.model.TaskUpdate;
import com.springboot.MyTodoList.repository.TaskUpdateRepository;

// Marks the class as a Spring service component, 
// allowing it to be automatically detected and instantiated by Spring container
// during component scanning.

@Service
public class TaskUpdateService {
    @Autowired
    private TaskUpdateRepository taskUpdateRepository;
    
    public TaskUpdateService(TaskUpdateRepository taskUpdateRepository){
        this.taskUpdateRepository = taskUpdateRepository;
    }    

    public String createNewTaskUpdate(TaskUpdate taskUpdate){
        try{
            taskUpdateRepository.save(taskUpdate);
            return "Task Update log with timestamp " + taskUpdate.getTimeStamp() + " created successfuly";
        }
        catch(Exception e){
            return "Task Update log with timestamp " + taskUpdate.getTimeStamp() + " fail. " + e.toString();
        }
    }

}
