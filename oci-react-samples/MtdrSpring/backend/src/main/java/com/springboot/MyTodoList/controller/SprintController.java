package com.springboot.MyTodoList.controller;
import com.springboot.MyTodoList.model.Sprint;
import com.springboot.MyTodoList.service.SprintService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class SprintController {
    @Autowired
    private SprintService SprintService;
    //@CrossOrigin

    // ## Get ##
    @GetMapping(value = "/sprint/{id}")
    public ResponseEntity<Sprint> getItemById(@PathVariable int id){
        try{
            ResponseEntity<Sprint> responseEntity = SprintService.getItemById(id);
            return new ResponseEntity<Sprint>(responseEntity.getBody(), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // ## Update ##
    @PutMapping(value = "/sprint/{id}")
    public ResponseEntity<Sprint> updateSprint(@PathVariable int id, @RequestBody Sprint td){
        try{
            Sprint sprintItem = SprintService.updateSprint(id, td);
            System.out.println(sprintItem.toString());
            return new ResponseEntity<>(sprintItem,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
}
