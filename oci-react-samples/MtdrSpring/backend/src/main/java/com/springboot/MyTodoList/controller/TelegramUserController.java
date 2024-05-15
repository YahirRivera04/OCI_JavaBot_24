package com.springboot.MyTodoList.controller;
import com.springboot.MyTodoList.model.TelegramUser;
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
    //@CrossOrigin

    // ## Get ##
    @GetMapping(value = "/telegramuser/{id}")
    public ResponseEntity<TelegramUser> getItemById(@PathVariable int id){
        try{
            ResponseEntity<TelegramUser> responseEntity = TelegramUserService.getItemById(id);
            return new ResponseEntity<TelegramUser>(responseEntity.getBody(), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
