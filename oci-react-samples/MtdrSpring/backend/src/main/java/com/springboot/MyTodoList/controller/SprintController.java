package com.springboot.MyTodoList.controller;
import com.springboot.MyTodoList.model.Project;
import com.springboot.MyTodoList.model.Sprint;
import com.springboot.MyTodoList.service.SprintService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.mockito.Mockito.spy;

import java.net.URI;
import java.util.List;

@RestController
public class SprintController {
    @Autowired
    private SprintService sprintService;
    
     // ##################### Sprint Controller Metods ##################### //
    
    // --------------------- Get All Projects ---------------------
    @GetMapping(value = "/sprints/")
    public ResponseEntity<String> findSprints(){
        String info = "";
        try{
            List<Sprint> sprintList = sprintService.findAllSprints();
            for(int i = 0; i < sprintList.size(); i++){
                info += sprintList.get(i).toString() + "\n";
            }
            return ResponseEntity.ok(info);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // --------------------- Post Project ---------------------
    @GetMapping(value = "/sprint/{Sprint}")
    public ResponseEntity<String> createSprint(@PathVariable Sprint sprint){
        return ResponseEntity.ok(sprintService.createNewSprint(sprint));
    }

    // ##################### Bot Controller Metods ##################### //

    // Get All projects
	public ResponseEntity<List<Sprint>> findAllSprints(){
		return ResponseEntity.ok(sprintService.findAllSprints());
	}

}
