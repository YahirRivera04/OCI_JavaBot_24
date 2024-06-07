package com.springboot.MyTodoList.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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

    // Print All User Type
    public String printUserTypeList(UserType userType){
        // Print all information form user type
        String userInfo = "Id " + userType.getID().toString() + 
        " \nName " + userType.getName() + 
        " \nDescription " + userType.getDescription();
        return userInfo;
    }

}
