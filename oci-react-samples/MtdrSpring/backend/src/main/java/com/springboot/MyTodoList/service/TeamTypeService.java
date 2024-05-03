package com.springboot.MyTodoList.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import com.springboot.MyTodoList.model.TeamType;
import com.springboot.MyTodoList.repository.TeamTypeRepository;

// Marks the class as a Spring service component, 
// allowing it to be automatically detected and instantiated by Spring container
// during component scanning.

@Service
public class TeamTypeService {
    @Autowired
    private TeamTypeRepository repository;
    
    // --------------------- Read Method ---------------------

    public ResponseEntity<TeamType> getItemById(int id){
        Optional<TeamType> data = repository.findById(id);
        if (data.isPresent()){
            return new ResponseEntity<>(data.get(), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // --------------------- Update Method ---------------------

    public TeamType updateTeamType(int id, TeamType td) {
        Optional<TeamType> data = repository.findById(id);
        if(data.isPresent()){
            TeamType teamType = data.get();
            // teamType.setID(id);
            // teamType.setCreation_ts(td.getCreation_ts());
            // teamType.setDescription(td.getDescription());
            // teamType.setDone(td.isDone());
            return repository.save(teamType);
        }else{
            return null;
        }
    }

}
