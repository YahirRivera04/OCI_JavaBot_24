package com.springboot.MyTodoList.controller;
import com.springboot.MyTodoList.model.SprintUpdate;
import com.springboot.MyTodoList.service.SprintUpdateService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class SprintUpdateController {
    @Autowired
    private SprintUpdateService sprintUpdateService;
    //@CrossOrigin

    // ## Get ##
    @GetMapping(value = "/sprintupdate/{id}")
    public ResponseEntity<SprintUpdate> getItemById(@PathVariable int id){
        try{
            ResponseEntity<SprintUpdate> responseEntity = sprintUpdateService.getItemById(id);
            return new ResponseEntity<SprintUpdate>(responseEntity.getBody(), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // ## Update ##
    @PutMapping(value = "sprintupdate/{id}")
    public ResponseEntity<SprintUpdate> updateSprintUpdate(@PathVariable int id, @RequestBody SprintUpdate td){
        try{
            SprintUpdate sprintUpdateItem = sprintUpdateService.updateSprintUpdate(id, td);
            System.out.println(sprintUpdateItem.toString());
            return new ResponseEntity<>(sprintUpdateItem,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
}
