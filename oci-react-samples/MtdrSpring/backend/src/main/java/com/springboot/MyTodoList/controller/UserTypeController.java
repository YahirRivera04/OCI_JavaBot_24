package com.springboot.MyTodoList.controller;
import com.springboot.MyTodoList.model.UserType;
import com.springboot.MyTodoList.service.UserTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping ("api/usertype")
public class UserTypeController {
    @Autowired
    private UserTypeService userTypeService;

    // ##################### User Type Controller Metods ##################### //

    // --------------------- Get All User Type Method ---------------------
    @GetMapping(value = "/allusertype/")
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
    
    


}
