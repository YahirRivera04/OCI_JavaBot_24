package com.springboot.MyTodoList.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.MyTodoList.model.Team;
import com.springboot.MyTodoList.model.TeamType;
import com.springboot.MyTodoList.service.TeamTypeService;

@RestController
public class TeamTypeController {
    @Autowired
    private TeamTypeService TeamTypeService;
    //@CrossOrigin

    // ## Get ##
    @GetMapping(value = "/teamtype/{id}")
    public ResponseEntity<TeamType> getItemById(@PathVariable int id){
        try{
            ResponseEntity<TeamType> responseEntity = TeamTypeService.getItemById(id);
            return new ResponseEntity<TeamType>(responseEntity.getBody(), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // ## Update ##
    @PutMapping(value = "taskstatus/{id}")
    public ResponseEntity<TeamType> updateTeamType(@PathVariable int id, @RequestBody TeamType td){
        try{
            TeamType taskItem = TeamTypeService.updateTeamType(id, td);
            System.out.println(taskItem.toString());
            return new ResponseEntity<>(taskItem,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
}
