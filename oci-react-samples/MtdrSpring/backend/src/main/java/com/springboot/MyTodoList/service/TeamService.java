package com.springboot.MyTodoList.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import com.springboot.MyTodoList.model.Team;
import com.springboot.MyTodoList.repository.TeamRepository;

// Marks the class as a Spring service component, 
// allowing it to be automatically detected and instantiated by Spring container
// during component scanning.

@Service
public class TeamService {
    @Autowired
    private TeamRepository teamRepository;
    
    public TeamService(TeamRepository teamRepository){
        this.teamRepository = teamRepository;
    }

    // --------------------- Get All Teams Method ---------------------
    public List<Team> findAllTeams(){
        return teamRepository.findAll();
    }
}
