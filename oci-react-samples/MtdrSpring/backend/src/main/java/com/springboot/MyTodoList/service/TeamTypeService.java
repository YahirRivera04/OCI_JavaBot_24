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
    private TeamTypeRepository teamTypeRepository;

    public TeamTypeService(TeamTypeRepository teamTypeRepository){
        this.teamTypeRepository = teamTypeRepository;
    }

    // --------------------- Get All User Type Method ---------------------
    public List<TeamType> findAllTeamTypes(){
        return teamTypeRepository.findAll();
    }
    


}
