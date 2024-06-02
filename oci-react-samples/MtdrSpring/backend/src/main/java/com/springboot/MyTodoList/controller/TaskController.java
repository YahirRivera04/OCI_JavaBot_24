package com.springboot.MyTodoList.controller;
import com.springboot.MyTodoList.model.Sprint;
import com.springboot.MyTodoList.model.Task;
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

    // ##################### Bot Controller Metods ##################### //

   // Get All Tasks
    public ResponseEntity<List<Task>> findAllTasks(){
        return ResponseEntity.ok(taskService.findAllTask());
    }

    public ResponseEntity<List<Task>> findAllTaskByTelegramUserId(Iterable<Long> telegramUserId){
        return ResponseEntity.ok(taskService.findAllTaskByTelegramUserId(telegramUserId));
    }

    public ResponseEntity<String> createTask(Task newTask){
        return ResponseEntity.ok(taskService.createTask(newTask));
    }



    // Print All Sprints
    public String printTask(Task task){
        String taskInfo = "Id " + task.getID().toString() +
                " \nName " + task.getName() + 
                " \nDescription " + task.getDescription() + 
                " \nEstimated Hours " + task.getEstimatedHours() + 
                " \nPriority " + task.getPriority() + 
                " \nTelegram User Id " + task.getTelegramUser().getID() + 
                " \nSprint Id " + task.getSprint().getID() + 
                " \nTask Status Id " + task.getTaskStatus().getID();

        return taskInfo;
    }

    
    

}
