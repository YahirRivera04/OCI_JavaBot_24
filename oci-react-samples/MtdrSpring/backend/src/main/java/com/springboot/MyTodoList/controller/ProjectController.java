package com.springboot.MyTodoList.controller;
import com.springboot.MyTodoList.model.Project;
import com.springboot.MyTodoList.service.ProjectService;
import com.springboot.MyTodoList.controller.BotController;

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
    private ProjectService projectService;

    @Autowired
    private BotController botController;

    // ##################### Project Controller Metods ##################### //
    
    // --------------------- Get All Projects ---------------------
    @GetMapping(value = "/project/")
    public ResponseEntity<String> findProjects(){
        String info = "";
        try{
            List<Project> projectList = projectService.findAllProjects();
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
        return ResponseEntity.ok(projectService.createNewProject(project));
    }

    // ##################### Bot Controller Metods ##################### //

    // Get All projects
	public ResponseEntity<List<Project>> findAllProjects(){
		return ResponseEntity.ok(projectService.findAllProjects());
	}

	// Create new project 
    // to do


    // Print All Projects
    public void printProjectList(Long chatId){
        List<Project> projectList = List.of(new Project());
        projectList = findAllProjects().getBody();
        // Print all information form project
        if(projectList != null){
            for(int i = 0; i < projectList.size(); i++){
                botController.sendMessage("Id " + projectList.get(i).getID().toString() +
                " \nName " + projectList.get(i).getName() + 
                " \nDescription " + projectList.get(i).getDescription(), chatId);
            }
        }
    }

}