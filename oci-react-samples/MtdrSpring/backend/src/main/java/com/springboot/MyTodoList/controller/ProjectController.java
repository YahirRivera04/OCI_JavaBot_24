package com.springboot.MyTodoList.controller;
import com.springboot.MyTodoList.model.Project;
import com.springboot.MyTodoList.service.ProjectService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController
public class ProjectController {
    @Autowired
    private ProjectService projectServices;
    //@CrossOrigin
    
    // ## Get ##
    @GetMapping(value = "/taskstatus/{id}")
    public ResponseEntity<Project> getItemById(@PathVariable int id){
        try{
            ResponseEntity<Project> responseEntity = projectServices.getItemById(id);
            return new ResponseEntity<Project>(responseEntity.getBody(), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // ## Update ##
    @PutMapping(value = "taskstatus/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable int id, @RequestBody Project td){
        try{
            Project projectItem = projectServices.updateProject(id, td);
            System.out.println(projectItem.toString());
            return new ResponseEntity<>(projectItem,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }


}