package com.springboot.MyTodoList.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import com.springboot.MyTodoList.model.Sprint;
import com.springboot.MyTodoList.repository.SprintRepository;

// Marks the class as a Spring service component, 
// allowing it to be automatically detected and instantiated by Spring container
// during component scanning.

@Service
public class SprintService {
    @Autowired
    private SprintRepository sprintRepository;

    public SprintService(SprintRepository sprintRepository){
        this.sprintRepository = sprintRepository;
    }
    
       // --------------------- Get All Sprint Method ---------------------
    public List<Sprint> findAllSprints(){
        return sprintRepository.findAll();
    }

    // --------------------- Create New Sprint Method ---------------------
    public String createNewSprint(Sprint newSprint){
        try{
            sprintRepository.save(newSprint);
            return "Sprint " + newSprint.getName() + " created succesfully.";
        }
        catch (Exception e){
            return "Sprint " + newSprint.getName() + " fail. \nERROR: " + e;   
        }
    }

}
