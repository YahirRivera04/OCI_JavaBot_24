package com.springboot.MyTodoList.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import com.springboot.MyTodoList.model.Project;
import com.springboot.MyTodoList.model.Sprint;
import com.springboot.MyTodoList.repository.SprintRepository;

// Marks the class as a Spring service component, 
// allowing it to be automatically detected and instantiated by Spring container
// during component scanning.

@Service
public class SprintService {
    @Autowired
    private SprintRepository sprintRepository;

    @Autowired
    private ProjectService projectService;
    
       // --------------------- Get All Sprint Method ---------------------
    public List<Sprint> findAllSprints(){
        List<Project> projectList = List.of(new Project());
        projectList = projectService.findAllProjects();
        
        List<Sprint> sprintList = List.of(new Sprint());
        sprintList = sprintRepository.findAll();

        if(projectList != null && sprintList != null){
            for(int i = 0; i < sprintList.size(); i++){
                for(int j = 0; i < projectList.size(); j++){
                    if(sprintList.get(i).getProject().getID() == projectList.get(j).getID()){
                        sprintList.get(i).setProject(projectList.get(j));
                    }
                }
            }
        }

        return sprintList;
    }

    // --------------------- Create New Sprint Method ---------------------
    public String createNewSprint(Sprint newSprint){
        try{
            sprintRepository.save(newSprint);
            return "Project " + newSprint.getName() + " created succesfully";
        }
        catch (Exception e){
            return "Project " + newSprint.getName() + " fail in creation " + e;   
        }
    }

}
