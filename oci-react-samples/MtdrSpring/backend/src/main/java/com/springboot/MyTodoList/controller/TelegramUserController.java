package com.springboot.MyTodoList.controller;
import com.springboot.MyTodoList.model.TelegramUser;
import com.springboot.MyTodoList.model.ToDoItem;
import com.springboot.MyTodoList.service.TelegramUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class TelegramUserController {
    @Autowired
    private TelegramUserService TelegramUserService;


    @GetMapping
    public ResponseEntity<String> checkIfTableExists(){
        try {
            return new ResponseEntity<String>(TelegramUserService.checkIfTableExists(), HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<String>("Table does not exist" + e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    // ## Get All ##
    //@CrossOrigin
    @GetMapping(value = "/telegramuser")
    public List<TelegramUser> getAllTelegramUsers(){
        return TelegramUserService.findAllUsers();
    }

    // ## Get by TelegramName ##
    @GetMapping(value = "/telegramuser/{TelegramName}")
    public ResponseEntity<Boolean> getUserByTelegramName(@PathVariable String TelegramName){
            return ResponseEntity.ok(TelegramUserService.existsByTelegramName(TelegramName));
        
    }

    // // ## Get by Id ##
    // @GetMapping(value = "/telegramuser/{id}")
    // public ResponseEntity<TelegramUser> getItemById(@PathVariable int id){
    //     try{
    //         ResponseEntity<TelegramUser> responseEntity = TelegramUserService.getItemById(id);
    //         return new ResponseEntity<TelegramUser>(responseEntity.getBody(), HttpStatus.OK);
    //     }catch (Exception e){
    //         return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    //     }
    // }

    

}
