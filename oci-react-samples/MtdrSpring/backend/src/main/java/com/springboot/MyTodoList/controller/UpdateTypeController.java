package com.springboot.MyTodoList.controller;
import com.springboot.MyTodoList.model.UpdateType;
import com.springboot.MyTodoList.service.UpdateTypeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class UpdateTypeController {
    @Autowired
    private UpdateTypeService UpdateTypeService;
    //@CrossOrigin

    // ## Get ##
    @GetMapping(value = "/taskstatus/{id}")
    public ResponseEntity<UpdateType> getItemById(@PathVariable int id){
        try{
            ResponseEntity<UpdateType> responseEntity = UpdateTypeService.getItemById(id);
            return new ResponseEntity<UpdateType>(responseEntity.getBody(), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // ## Update ##
    @PutMapping(value = "taskstatus/{id}")
    public ResponseEntity<UpdateType> updateUpdateType(@PathVariable int id, @RequestBody UpdateType td){
        try{
            UpdateType updateItem = UpdateTypeService.updateUpdateType(id, td);
            System.out.println(updateItem.toString());
            return new ResponseEntity<>(updateItem,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

}
