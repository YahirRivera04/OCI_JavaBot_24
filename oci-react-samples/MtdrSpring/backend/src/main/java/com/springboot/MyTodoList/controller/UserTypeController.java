package com.springboot.MyTodoList.controller;
import com.springboot.MyTodoList.model.UserType;
import com.springboot.MyTodoList.service.UserTypeService;

import com.springboot.MyTodoList.controller.BotController;

import org.apache.tomcat.jni.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class UserTypeController {
    @Autowired
    private UserTypeService userTypeService;

    @Autowired
    private BotController botController;

    // ##################### User Type Controller Metods ##################### //

    // --------------------- Get All User Type Method ---------------------
    @GetMapping(value = "/usertype/")
    public ResponseEntity<String> findUserType(){
         String info = "";
        try{
            List<UserType> userTypeList = userTypeService.findAllUserType();
            for(int i = 0; i < userTypeList.size(); i++){
                info += userTypeList.get(i).toString() + "\n";
            }
            return ResponseEntity.ok(info);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    // ##################### Bot Controller Metods ##################### //

    // Get All User Types
	public ResponseEntity<List<UserType>> findAllUserType(){
		return ResponseEntity.ok(userTypeService.findAllUserType());
	}

    // Get All User Type
    public List<UserType> getUserTypeList(){
        List<UserType> userTypeList = List.of(new UserType());
        userTypeList = findAllUserType().getBody();
        return userTypeList;
    }

    // Print All User Type
    public void printUserTypeList(Long chatId){
        List<UserType> userTypeList = List.of(new UserType());
        userTypeList = findAllUserType().getBody();
        // Print all information form project
        if(userTypeList != null){
            for(int i = 0; i < userTypeList.size(); i++){
                botController.sendMessage("Id " + userTypeList.get(i).getID().toString() +
                " \nName " + userTypeList.get(i).getName() + 
                " \nDescription " + userTypeList.get(i).getDescription(), chatId);
            }
        }
    }

}
