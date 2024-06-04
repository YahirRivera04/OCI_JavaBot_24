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

    // Get All Tasks By Telegram User Id
    public List<Task> findAllTaskByTelegramUserId(Long telegramUserId){
        return taskRepository.findAllTasksByTelegramUserId(telegramUserId);
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
    
    // Delete Task
    public String deleteTask(Long telegramUserId, String name){
        try{
            taskRepository.deleteTasksByTelegramUserIdAndTaskName(telegramUserId, name);
            return "Task deleted successfully";
        }
        catch(Exception e){
            return "Task fail to delete " + e.toString();
        }
        
    }

}
