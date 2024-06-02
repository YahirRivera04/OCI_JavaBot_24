package com.springboot.MyTodoList.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import com.springboot.MyTodoList.model.Task;
import com.springboot.MyTodoList.repository.TaskRepository;

// Marks the class as a Spring service component, 
// allowing it to be automatically detected and instantiated by Spring container
// during component scanning.

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;
    
    public TaskService(TaskRepository taskRepository){
        this.taskRepository = taskRepository;
    }

    // Get All Tasks
    public List<Task> findAllTaskByTelegramUserId(Iterable<Long> telegramUserId){
        return taskRepository.findAllById(telegramUserId);
    }

    // Get All Tasks
    public List<Task> findAllTask(){
        return taskRepository.findAll();
    }

    // Post Task
    public String createTask(Task task){
        try{
            taskRepository.save(task);
            return "Task " + task.getName() + " created succesfully.";
        }
        catch(Exception e){
            return "Task with Id " + task.getID() + " fail.\nERROR: " + e.toString();
        }
    }
    
}
