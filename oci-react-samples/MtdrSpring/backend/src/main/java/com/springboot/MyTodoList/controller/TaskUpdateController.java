package com.springboot.MyTodoList.controller;
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
    //@CrossOrigin

    // ## Get ##
    @GetMapping(value = "/taskupdate/{id}")
    public ResponseEntity<TaskUpdate> getItemById(@PathVariable int id){
        try{
            ResponseEntity<TaskUpdate> responseEntity = taskUpdateService.getItemById(id);
            return new ResponseEntity<TaskUpdate>(responseEntity.getBody(), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // ## Update ##
    @PutMapping(value = "taskupdate/{id}")
    public ResponseEntity<TaskUpdate> updateTaskUpdate(@PathVariable int id, @RequestBody TaskUpdate td){
        try{
            TaskUpdate taskUpdateItem = taskUpdateService.updateTaskUpdate(id, td);
            System.out.println(taskUpdateItem.toString());
            return new ResponseEntity<>(taskUpdateItem,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
}
