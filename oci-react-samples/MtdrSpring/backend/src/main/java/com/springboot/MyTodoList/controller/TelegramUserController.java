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
    @GetMapping(value = "/telegramuser/telegramuserexist/{TelegramName}")
    public ResponseEntity<Boolean> getUserByTelegramName(@PathVariable String TelegramName){
            return ResponseEntity.ok(TelegramUserService.existsByTelegramName(TelegramName));
    }


    // ## Get telegram id by telegram user name ##

    @GetMapping(value = "/telegramuser/telegramuserid/{TelegramName}")
    public ResponseEntity<Integer> getTelegramUserId(@PathVariable String TelegramName){
        return ResponseEntity.ok(TelegramUserService.findTelegramUserId(TelegramName));
    }

    // ## Get Chat Id by Telegram User Id ##

    @GetMapping(value = "/telegramuser/telegramchatid/{TelegramName}")
    public ResponseEntity<Long> findChatId(@PathVariable Long id){
        return ResponseEntity.ok(TelegramUserService.fndChatIdByTelegramUserId(id));
    }

    // ## Post ChatId ##
    //@CrossOrigin
    @PutMapping(value = "telegramuser/setid/{TelegramUserId}")
    public ResponseEntity<String> updateTelegramUser(@RequestBody TelegramUser telegramUser, @PathVariable Long id){
        return ResponseEntity.ok(TelegramUserService.updateChatId(id, telegramUser));
    }
}
