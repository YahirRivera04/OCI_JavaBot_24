package com.springboot.MyTodoList.service;
import org.apache.tomcat.jni.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import com.springboot.MyTodoList.model.UserType;
import com.springboot.MyTodoList.repository.UserTypeRepository;


@Service
public class UserTypeService {
    @Autowired
    private final UserTypeRepository userTypeRepository;

    public UserTypeService(UserTypeRepository userTypeRepository){
        this.userTypeRepository = userTypeRepository;
    }
    
    // --------------------- Get All User Type Method ---------------------
    public List<UserType> findAllUserType(){
        return userTypeRepository.findAll();
    }

}
