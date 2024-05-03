package com.springboot.MyTodoList.controller;
import com.springboot.MyTodoList.model.Team;
import com.springboot.MyTodoList.service.TeamService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class TeamController {
    @Autowired
    private TeamService TeamService;
    //@CrossOrigin

    // ## Get ##
    @GetMapping(value = "/team/{id}")
    public ResponseEntity<Team> getItemById(@PathVariable int id){
        try{
            ResponseEntity<Team> responseEntity = TeamService.getItemById(id);
            return new ResponseEntity<Team>(responseEntity.getBody(), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // ## Update ##
    @PutMapping(value = "team/{id}")
    public ResponseEntity<Team> updateTeam(@PathVariable int id, @RequestBody Team td){
        try{
            Team teamItem = TeamService.updateTeam(id, td);
            System.out.println(teamItem.toString());
            return new ResponseEntity<>(teamItem,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
}
