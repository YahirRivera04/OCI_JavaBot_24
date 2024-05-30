package com.springboot.MyTodoList.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import com.springboot.MyTodoList.model.TaskStatus;
import com.springboot.MyTodoList.repository.TaskStatusRepository;

// Marks the class as a Spring service component, 
// allowing it to be automatically detected and instantiated by Spring container
// during component scanning.

@Service
public class TaskStatusService {
    @Autowired
    private TaskStatusRepository taskStatusRepository;
    
    // --------------------- Get All Task Status Methods  ---------------------        
    public List<TaskStatus> findAllTaskStatus(){
        return taskStatusRepository.findAll();
    }
}
