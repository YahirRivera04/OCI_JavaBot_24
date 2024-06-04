package com.springboot.MyTodoList.controller;
import com.springboot.MyTodoList.model.Sprint;
import com.springboot.MyTodoList.model.Task;
import com.springboot.MyTodoList.model.TelegramUser;
import com.springboot.MyTodoList.service.TaskService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class TaskController {
    @Autowired
    private TaskService taskService;
    
    // ##################### Task Controller Metods ##################### //

    // --------------------- Get All Tasks ---------------------
    @GetMapping(value = "/tasks/")
    public ResponseEntity<String> findTasks(){
        String info = "";
        try{
            List<Task> taskList = taskService.findAllTask();
            for(int i = 0; i < taskList.size(); i++){
                info += taskList.get(i).toString() + "\n";
            }
            return ResponseEntity.ok(info);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // --------------------- Get All Tasks By TelegramUserId ---------------------
    @GetMapping(value = "/tasks/{TelegramUserId}")
    public ResponseEntity<String> findTaskByTelegramUserId(@PathVariable Long telegramUserId){
        String info = "";
        try{
            List<Task> taskList = taskService.findAllTaskByTelegramUserId(telegramUserId);
            for(int i = 0; i < taskList.size(); i++){
                info += taskList.get(i).toString() + "\n";
            }
            return ResponseEntity.ok(info);
        }
        catch(Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // --------------------- Create Task ---------------------
    @GetMapping(value = "/tasks/{Task}")
    public ResponseEntity<String> createNewTask(@PathVariable Task newTask){
        String info = "";
        try{
            info = taskService.createTask(newTask);
            return ResponseEntity.ok(info);
        }
        catch(Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // --------------------- Delete Task ---------------------
    @GetMapping(value = "/tasks/{telegramUserId}/{name}")
    public ResponseEntity<String> findTaskByTelegramUserId(@PathVariable Long telegramUserId, @PathVariable String name){
        String info = "";
        try{
            info = taskService.deleteTask(telegramUserId, name);
            return ResponseEntity.ok(info);
        }
        catch(Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // ##################### Bot Controller Metods ##################### //

   // Get All Tasks
    public ResponseEntity<List<Task>> findAllTasks(){
        return ResponseEntity.ok(taskService.findAllTask());
    }
    // Get All Tasks By Telegram User Id
    public ResponseEntity<List<Task>> findAllTaskByTelegramUserId(Long telegramUserId){
        return ResponseEntity.ok(taskService.findAllTaskByTelegramUserId(telegramUserId));
    }
    // Post Task
    public ResponseEntity<String> createTask(Task newTask){
        return ResponseEntity.ok(taskService.createTask(newTask));
    }
    // Delete Task
    public ResponseEntity<String> deleteTask(Long telegramUserId, String name){
        return ResponseEntity.ok(taskService.deleteTask(telegramUserId, name));
    }

    // Print All Tasks
    public String printTask(Task task){
        String taskInfo = "Name " + task.getName() + 
                " \nDescription " + task.getDescription() + 
                " \nEstimated Hours " + task.getEstimatedHours() + 
                " \nPriority " + task.getPriority() + 
                " \nTelegram User Id " + task.getTelegramUser().getID() + 
                " \nSprint Id " + task.getSprint().getID() + 
                " \nTask Status Id " + task.getTaskStatus().getID();

        return taskInfo;
    }

    
    

}
