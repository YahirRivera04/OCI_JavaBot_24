package com.springboot.MyTodoList.controller;
import com.springboot.MyTodoList.model.UpdateType;
import com.springboot.MyTodoList.service.UpdateTypeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UpdateTypeController {
    @Autowired
    private UpdateTypeService updateTypeService;
   
    // ##################### Update Type Controller Metods ##################### //

    // --------------------- Get All User Type Method ---------------------
    @GetMapping(value = "/updatetype/")
    public ResponseEntity<String> findUpdateType(){
         String info = "";
        try{
            List<UpdateType> updateTypeList = updateTypeService.findAllUpdateTypes();
            for(int i = 0; i < updateTypeList.size(); i++){
                info += updateTypeList.get(i).toString() + "\n";
            }
            return ResponseEntity.ok(info);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
}
