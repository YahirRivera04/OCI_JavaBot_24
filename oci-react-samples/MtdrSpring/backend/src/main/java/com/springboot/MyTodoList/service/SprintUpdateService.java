package com.springboot.MyTodoList.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.springboot.MyTodoList.model.SprintUpdate;
import com.springboot.MyTodoList.repository.SprintUpdateRepository;

// Marks the class as a Spring service component, 
// allowing it to be automatically detected and instantiated by Spring container
// during component scanning.

@Service
public class SprintUpdateService {
    @Autowired
    private SprintUpdateRepository sprintUpdateRepository;
    
    public SprintUpdateService(SprintUpdateRepository sprintUpdateRepository){
        this.sprintUpdateRepository = sprintUpdateRepository;
    }

    // --------------------- Create New Sprint Update Method ---------------------
    public String createNewSprintUpdate(SprintUpdate newSprintUpdate){
        try{
            sprintUpdateRepository.save(newSprintUpdate);
            return "Sprint Update with timestamp " + newSprintUpdate.getTimeStamp() + " created succesfully.";
        }
        catch (Exception e){
            return "Sprint Update with timestamp " + newSprintUpdate.getTimeStamp() + " fail. \nERROR: " + e;   
        }
    }
  
    // Print Sprint Update Info
    public String printSptintUpdate(SprintUpdate sprintUpdate){
        String sprintUpdateInfo = "Time Stamp: " + sprintUpdate.getTimeStamp() + 
        " \nUpdate Type Id: " + sprintUpdate.getUpdateType().getID() +
        " \nSprint Id: " + sprintUpdate.getSprint().getID() +
        " \nUser Id: " + sprintUpdate.getTelegramUser().getID();
        
        return sprintUpdateInfo;
    }

}
