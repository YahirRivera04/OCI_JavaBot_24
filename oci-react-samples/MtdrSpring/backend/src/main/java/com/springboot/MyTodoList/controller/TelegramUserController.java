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

    // ## Get All User Info by Telegram User Id ##

    @GetMapping(value = "/telegramuser/{TelegramUserId}")
    public ResponseEntity<TelegramUser> getUserInfo(@PathVariable Long telegramUserId){
        try{
            return ResponseEntity.ok(TelegramUserService.getUserInfo(telegramUserId));
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // ## Verify User by Telegram User Name ##
    @GetMapping(value = "/telegramuser/telegramuserexist/{TelegramName}")
    public ResponseEntity<Boolean> existByChatId(@PathVariable Long chatId){
            return ResponseEntity.ok(TelegramUserService.existByChatId(chatId));
    }


    // ## Get telegram id by Telegram User Name ##
    @GetMapping(value = "/telegramuser/telegramuserid/{TelegramName}")
    public ResponseEntity<Long> getTelegramUserId(@PathVariable String TelegramName){
        return ResponseEntity.ok(TelegramUserService.findTelegramUserId(TelegramName));
    }


    // ## Get Chat Id by Telegram User Id ##
    @GetMapping(value = "/telegramuser/chatid/{TelegramUserId}")
    public ResponseEntity<Long> findChatId(@PathVariable Long telegramUserId){
        try{
            return ResponseEntity.ok(TelegramUserService.findChatIdByTelegramUserId(telegramUserId));
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    // ## Post ChatId ##
    //@CrossOrigin
    @PutMapping(value = "telegramuser/setid/{TelegramUserId}")
    public ResponseEntity<String> updateTelegramUser( @PathVariable Long telegramUserId, @RequestBody Long chatId){
        try {
            return ResponseEntity.ok(TelegramUserService.updateChatId(telegramUserId, chatId));
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
