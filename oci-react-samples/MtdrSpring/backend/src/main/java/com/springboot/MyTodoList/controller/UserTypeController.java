package com.springboot.MyTodoList.controller;
import com.springboot.MyTodoList.model.UserType;
import com.springboot.MyTodoList.service.UserTypeService;

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
    private UserTypeService UserTypeService;
    //@CrossOrigin

    // ## Get ##
    @GetMapping(value = "/usertype/{id}")
    public ResponseEntity<UserType> getItemById(@PathVariable int id){
        try{
            ResponseEntity<UserType> responseEntity = UserTypeService.getItemById(id);
            return new ResponseEntity<UserType>(responseEntity.getBody(), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // ## Update ##
    @PutMapping(value = "usertype/{id}")
    public ResponseEntity updateUserType(@PathVariable int id, @RequestBody UserType td){
        try{
            UserType userTypeItem = UserTypeService.updateUserType(id, td);
            System.out.println(userTypeItem.toString());
            return new ResponseEntity<>(userTypeItem,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }


}
