package com.springboot.MyTodoList.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import com.springboot.MyTodoList.model.Project;
import com.springboot.MyTodoList.repository.ProjectRepository;

// Marks the class as a Spring service component, 
// allowing it to be automatically detected and instantiated by Spring container
// during component scanning.

@Service
public class ProjectService {
    @Autowired
    private ProjectRepository ProjectRepository;
    
    // --------------------- Read Method ---------------------

    public ResponseEntity<Project> getItemById(int id){
        Optional<Project> data = ProjectRepository.findById(id);
        if (data.isPresent()){
            return new ResponseEntity<>(data.get(), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // --------------------- Update Method ---------------------

    public Project updateTaskStatus(int id, Project td) {
        Optional<Project> data = ProjectRepository.findById(id);
        if(data.isPresent()){
            Project project = data.get();
            // project.setID(id);
            // project.setCreation_ts(td.getCreation_ts());
            // project.setDescription(td.getDescription());
            // project.setDone(td.isDone());
            return ProjectRepository.save(project);
        }else{
            return null;
        }
    }

}
