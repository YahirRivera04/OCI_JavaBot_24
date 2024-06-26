package com.springboot.MyTodoList.controller;
import com.springboot.MyTodoList.model.SprintUpdate;
import com.springboot.MyTodoList.service.SprintUpdateService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class SprintUpdateController {
    @Autowired
    private SprintUpdateService sprintUpdateService;
    
    // ##################### Sprint Update Controller Metods ##################### //

     // --------------------- Post Sprint Update ---------------------
    @GetMapping(value = "/sprint/{SprintUpdate}")
    public ResponseEntity<String> createSprintUpdate(@PathVariable SprintUpdate sprintUpdate){
        return ResponseEntity.ok(sprintUpdateService.createNewSprintUpdate(sprintUpdate));
    }

}
