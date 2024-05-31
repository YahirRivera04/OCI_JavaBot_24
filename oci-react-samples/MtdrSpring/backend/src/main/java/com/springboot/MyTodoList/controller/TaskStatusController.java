package com.springboot.MyTodoList.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.MyTodoList.model.TaskStatus;
import com.springboot.MyTodoList.service.TaskStatusService;

import java.util.List;

@RestController
public class TaskStatusController {
    @Autowired
    private TaskStatusService TaskStatusService; 

    // --------------------- Get All Task Status ---------------------
    @GetMapping(value = "/taskstatus/")
    public ResponseEntity<String> findTaskStatus(){
        String info = "";
        try{
            List<TaskStatus> taskStatus = TaskStatusService.findAllTaskStatus();
            for(int i = 0; i < taskStatus.size(); i++){
                info += taskStatus.get(i).toString() + "\n";
            }
            return ResponseEntity.ok(info);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
