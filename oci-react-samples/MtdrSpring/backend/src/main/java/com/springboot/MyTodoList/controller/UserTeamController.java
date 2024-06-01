package com.springboot.MyTodoList.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.springboot.MyTodoList.service.UserTeamService;
import com.springboot.MyTodoList.model.UserTeam;

import java.util.List;

@RestController
public class UserTeamController {
    @Autowired
    private UserTeamService userTeamService;

    // ##################### Team Controller Metods ##################### //
     @GetMapping(value = "/userteam/")
    public ResponseEntity<String> findUserTeams(){
        String info = "";
        try{
            List<UserTeam> userTeamList = userTeamService.findAllUserTeams();
            for(int i  = 0; i < userTeamList.size(); i++){
                info += userTeamList.get(i).toString() + "\n";
            }
            return ResponseEntity.ok(info);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }
    }

    // ##################### Bot Controller Metods ##################### //
    
    // Get All User Types
	public ResponseEntity<List<UserTeam>> findAllUserTeams(){
		return ResponseEntity.ok(userTeamService.findAllUserTeams());
	}

    // Print All User Type
    public String printUserTeamList(UserTeam userTeam){
        // Print all information form user type
        String userTeamInfo = "Id " + userTeam.getID().toString() + 
        " \nUser Team Id " + userTeam.getID() + 
        " \nTelegram User Id " + userTeam.getTelegramUser().getID() + 
        " \nTeam Id " + userTeam.getTeam().getID();
        return userTeamInfo;
    } 

}
