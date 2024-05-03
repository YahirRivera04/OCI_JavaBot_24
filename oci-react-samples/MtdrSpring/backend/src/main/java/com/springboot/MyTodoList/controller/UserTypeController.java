package com.springboot.MyTodoList.controller;
import com.springboot.MyTodoList.model.;
import com.springboot.MyTodoList.service.;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class UserTypeController {
    @Autowired
    private TaskStatusService TaskStatusService;
    //@CrossOrigin

    // ## Get ##
    @GetMapping(value = "/taskstatus/{id}")
    public ResponseEntity<TaskStatus> getItemById(@PathVariable int id){
        try{
            ResponseEntity<TaskStatus> responseEntity = TaskStatusService.getItemById(id);
            return new ResponseEntity<TaskStatus>(responseEntity.getBody(), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // ## Update ##
    @PutMapping(value = "taskstatus/{id}")
    public ResponseEntity<TaskStatus> updateTaskStatus(@PathVariable int id, @RequestBody TaskStatus td){
        try{
            TaskStatus taskItem = TaskStatusService.updateTaskStatus(id, td);
            System.out.println(taskItem.toString());
            return new ResponseEntity<>(taskItem,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }


}
