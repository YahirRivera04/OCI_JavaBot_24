package com.springboot.MyTodoList.controller;
import com.springboot.MyTodoList.model.Project;
import com.springboot.MyTodoList.model.TaskStatus;
import com.springboot.MyTodoList.service.ProjectService;
import com.springboot.MyTodoList.service.TaskStatusService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController
public class ProjectController {
    @Autowired
    private ProjectService projectServices;
    
    // --------------------- Get All Projects ---------------------
    @GetMapping(value = "/project/")
    public ResponseEntity<String> findProjects(){
        String info = "";
        try{
            List<Project> projectList = projectServices.findAllProjects();
            for(int i = 0; i < projectList.size(); i++){
                info += projectList.get(i).toString() + "\n";
            }
            return ResponseEntity.ok(info);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // --------------------- Post Project ---------------------
    @GetMapping(value = "/project/{Project}")
    public ResponseEntity<String> createProject(@PathVariable Project project){
        return ResponseEntity.ok(projectServices.createNewProject(project));
    }

}