package com.springboot.MyTodoList.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.Optional;

import com.springboot.MyTodoList.model.Team;
import com.springboot.MyTodoList.repository.TeamRepository;

// Marks the class as a Spring service component, 
// allowing it to be automatically detected and instantiated by Spring container
// during component scanning.

@Service
public class TeamService {
    @Autowired
    private TeamRepository repository;
    
    // --------------------- Read Method ---------------------

    public ResponseEntity<Team> getItemById(int id){
        Optional<Team> data = repository.findById(id);
        if (data.isPresent()){
            return new ResponseEntity<>(data.get(), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // --------------------- Update Method ---------------------

    public Team updateTeam(int id, Team td) {
        Optional<Team> data = repository.findById(id);
        if(data.isPresent()){
            Team team = data.get();
            // team.setID(id);
            // team.setCreation_ts(td.getCreation_ts());
            // team.setDescription(td.getDescription());
            // team.setDone(td.isDone());
            return repository.save(team);
        }else{
            return null;
        }
    }

}
