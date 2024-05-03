package com.springboot.MyTodoList.controller;
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
    private TaskService TaskService;
    //@CrossOrigin
    // ## Post ##
    @PostMapping(value = "/task")
    public ResponseEntity addTask(@RequestBody Task taskItem) throws Exception{
        Task td = TaskService.addTask(taskItem);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("location",""+td.getID());
        responseHeaders.set("Access-Control-Expose-Headers","location");
        //URI location = URI.create(""+td.getID())

        return ResponseEntity.ok()
                .headers(responseHeaders).build();
    }

    // ## Get ##
    @GetMapping(value = "/task/{id}")
    public ResponseEntity<Task> getItemById(@PathVariable int id){
        try{
            ResponseEntity<Task> responseEntity = TaskService.getItemById(id);
            return new ResponseEntity<Task>(responseEntity.getBody(), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // ## Update ##
    @PutMapping(value = "tasks/{id}")
    public ResponseEntity updateTask(@PathVariable int id, @RequestBody Task td){
        try{
            Task taskItem = TaskService.updateTask(id, td);
            System.out.println(taskItem.toString());
            return new ResponseEntity<>(taskItem,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
    // ## Delete ##
    @DeleteMapping(value = "task/{id}")
    public ResponseEntity<Boolean> deleteTask(@PathVariable("id") int id){
        Boolean flag = false;
        try{
            flag = TaskService.deleteTask(id);
            return new ResponseEntity<>(flag, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(flag,HttpStatus.NOT_FOUND);
        }
    }




    
    

}
