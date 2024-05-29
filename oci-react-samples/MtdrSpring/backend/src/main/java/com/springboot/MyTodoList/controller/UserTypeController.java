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

    // --------------------- Get User Type Info by Name Method ---------------------
    @GetMapping(value = "/usertype/usertypeinfo/{Name}")
    public ResponseEntity<Long> findUserTypeIdByName(@PathVariable String name){
        return ResponseEntity.ok(UserTypeService.findUserTypeIdByName(name));
    }


}
