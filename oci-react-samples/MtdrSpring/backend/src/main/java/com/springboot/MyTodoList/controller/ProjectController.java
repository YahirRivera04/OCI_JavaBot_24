package com.springboot.MyTodoList.controller;
import com.springboot.MyTodoList.model.Project;
import com.springboot.MyTodoList.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class ProjectController {
    @Autowired
    private ProjectService projectService;

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

    // --------------------- Create New Project ---------------------
    @GetMapping(value = "/project/{Project}")
    public ResponseEntity<String> createNewProject(@PathVariable Project project){
        return ResponseEntity.ok(projectService.createNewProject(project));
    }

    // ##################### Bot Controller Metods ##################### //

    // Get All projects
	public ResponseEntity<List<Project>> findAllProjects(){
		return ResponseEntity.ok(projectService.findAllProjects());
	}

	// Create new project 
    public ResponseEntity<String> createProject(@PathVariable Project project){
        return ResponseEntity.ok(projectService.createNewProject(project));
    }

    // Print Projects
    public String printProjectList(Project project){
        String projectInfo = "Id " + project.getID().toString() +
                " \nName " + project.getName() + 
                " \nDescription " + project.getDescription();

        return projectInfo;
    }

}