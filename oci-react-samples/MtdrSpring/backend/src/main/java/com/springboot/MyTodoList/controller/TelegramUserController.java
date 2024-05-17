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

    // ## Check Table Exists ##
    @GetMapping
    public ResponseEntity<String> checkIfTableExists(){
        try {
            return new ResponseEntity<String>(TelegramUserService.checkIfTableExists(), HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<String>("Table does not exist" + e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // ## Verify User by TelegramName ##
    @GetMapping(value = "/telegramuser/{TelegramName}")
    public ResponseEntity<Boolean> getUserByTelegramName(@PathVariable String TelegramName){
            return ResponseEntity.ok(TelegramUserService.existsByTelegramName(TelegramName));
    }

    // ## Get all Telegram User Info by Telegram Name ##
    @GetMapping(value = "/telegramuser/telegramuserinfo/{TelegramName}")
    public ResponseEntity<TelegramUser> getTelegramUserInfo(@PathVariable String TelegramName){
        try{
            return new ResponseEntity<>(TelegramUserService.getTelegramUserInfo(TelegramName).getFirst(), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    // ## Post ChatId ##
    //@CrossOrigin
    @PutMapping(value = "telegramuser/{TelegramUserId}")
    public ResponseEntity updateTelegramUser(@RequestBody TelegramUser telegramUser, @PathVariable Long id){
        try{
            TelegramUser user = TelegramUserService.updateTelegramUser(id, telegramUser);
            System.out.println(user.toString());
            return new ResponseEntity<>(user,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

    }
}
