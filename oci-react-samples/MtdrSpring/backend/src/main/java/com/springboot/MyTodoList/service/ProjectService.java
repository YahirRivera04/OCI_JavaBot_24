package com.springboot.MyTodoList.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import com.springboot.MyTodoList.model.Project;
import com.springboot.MyTodoList.repository.ProjectRepository;

// Marks the class as a Spring service component, 
// allowing it to be automatically detected and instantiated by Spring container
// during component scanning.

@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository){
        this.projectRepository = projectRepository;
    }
    
    // --------------------- Get All Projects Method ---------------------
    public List<Project> findAllProjects(){
        return projectRepository.findAll();
    }

    // --------------------- Create New Project Method ---------------------
    public String createNewProject(Project newProject){
        try{
            projectRepository.save(newProject);
            return "Project " + newProject.getName() + "created succesfully";
        }
        catch (Exception e){
            return "Project " + newProject.getName() + "fail in creation. \nERROR: " + e;   
        }
    }
}
