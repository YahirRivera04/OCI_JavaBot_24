package com.springboot.MyTodoList.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import com.springboot.MyTodoList.model.UserTeam;
import com.springboot.MyTodoList.repository.UserTeamRepository;

// Marks the class as a Spring service component, 
// allowing it to be automatically detected and instantiated by Spring container
// during component scanning.

@Service
public class UserTeamService {
    @Autowired
    private UserTeamRepository userTeamRepository;
    
    public UserTeamService (UserTeamRepository userTeamRepository){
        this.userTeamRepository = userTeamRepository;
    }

    // --------------------- Get All UserTeam Method ---------------------
    public List<UserTeam> findAllUserTeams(){
        return userTeamRepository.findAll();
    }
}
