package com.springboot.MyTodoList.controller;
import com.springboot.MyTodoList.model.Team;
import com.springboot.MyTodoList.service.TeamService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TeamController {
    @Autowired
    private TeamService teamService;

    // ##################### Team Controller Metods ##################### //
     @GetMapping(value = "/team/")
    public ResponseEntity<String> findTeam(){
        String info = "";
        try{
            List<Team> teamList = teamService.findAllTeams();
            for(int i  = 0; i < teamList.size(); i++){
                info += teamList.get(i).toString() + "\n";
            }
            return ResponseEntity.ok(info);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }
    }

    // ##################### Bot Controller Metods ##################### //
    
    // Get All User Types
	public ResponseEntity<List<Team>> findAllTeams(){
		return ResponseEntity.ok(teamService.findAllTeams());
	}

    // Print All User Type
    public String printTeamTypeList(Team team){
        // Print all information form user type
        String teamInfo = "Id " + team.getID().toString() + 
        " \nName " + team.getName() + 
        " \nDescription " + team.getDescription();
        return teamInfo;
    } 

}
