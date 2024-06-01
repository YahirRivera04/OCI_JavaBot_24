package com.springboot.MyTodoList.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.MyTodoList.model.TeamType;
import com.springboot.MyTodoList.service.TeamTypeService;

@RestController
public class TeamTypeController {
    @Autowired
    private TeamTypeService teamTypeService;

    // ##################### Update Type Controller Metods ##################### //
    @GetMapping(value = "/teamtype/")
    public ResponseEntity<String> findTeamType(){
        String info = "";
        try{
            List<TeamType> teamTypeList = teamTypeService.findAllTeamTypes();
            for(int i  = 0; i < teamTypeList.size(); i++){
                info += teamTypeList.get(i).toString() + "\n";
            }
            return ResponseEntity.ok(info);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }
    }

    // ##################### Bot Controller Metods ##################### //
    
    // Get All User Types
	public ResponseEntity<List<TeamType>> findAllTeamType(){
		return ResponseEntity.ok(teamTypeService.findAllTeamTypes());
	}

    // Print All User Type
    public String printTeamTypeList(TeamType teamType){
        // Print all information form user type
        String teamTypeInfo = "Id " + teamType.getID().toString() + 
        " \nName " + teamType.getName() + 
        " \nDescription " + teamType.getDescription();
        return teamTypeInfo;
    } 
   
}
