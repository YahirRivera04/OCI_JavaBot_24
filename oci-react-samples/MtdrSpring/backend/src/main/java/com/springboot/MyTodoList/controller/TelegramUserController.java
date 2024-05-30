package com.springboot.MyTodoList.controller;
import com.springboot.MyTodoList.model.TelegramUser;
import com.springboot.MyTodoList.service.TelegramUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.mockito.ArgumentMatchers.refEq;

import java.net.URI;
import java.util.List;


@RestController
public class TelegramUserController {
    @Autowired
    private TelegramUserService TelegramUserService;

    // --------------------- Exist Chat Id by Chat Id Method ---------------------
    // /telegramuser/chatid/1984472475
    @GetMapping(value = "/telegramuser/chatid/{chatId}")
    public ResponseEntity<String> findChatIdByChatId(@PathVariable Long chatId){
        try{
            // Store variable to check if chatId exists
            Long chatIdLong = TelegramUserService.findChatIdByChatId(chatId);
            String chatIdString = Long.toString(chatIdLong);
            return ResponseEntity.ok("ChatId: " + chatIdString);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // --------------------- Get Telegram User ID by Telegram User name Method  ---------------------
    // /telegramuser/telegramuserid/Yahir_Rivera04
    @GetMapping(value = "/telegramuser/telegramuserid/{TelegramName}")
    public ResponseEntity<String> getTelegramUserId(@PathVariable String TelegramName){
        try{
            Long telegramUserIdLong = TelegramUserService.findTelegramUserId(TelegramName);
            String telegramUserIdString = Long.toString(telegramUserIdLong);
            return ResponseEntity.ok("TelegramUserId: " + telegramUserIdString);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    // --------------------- Update ChatId Method ---------------------
    //@CrossOrigin
    @PutMapping(value = "telegramuser/setchatid/{TelegramUserId}")
    public ResponseEntity<String> updateTelegramUser( @PathVariable Long telegramUserId, @RequestBody Long chatId){
        try {
            return ResponseEntity.ok(TelegramUserService.updateChatId(telegramUserId, chatId));
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // --------------------- Get Telegram User Id by Chat Id Method  ---------------------
    // /telegramuser/telegramuserid/1984472475
    @GetMapping(value = "telegramuser/telegramuserid/{chatId}")
    public ResponseEntity<String> findUserIdByChatId (@PathVariable Long chatId){
        try{
            Long telegramUserIdLong = TelegramUserService.findUserIdByChatId(chatId);
            String telegramUserIdString = Long.toString(telegramUserIdLong);
            return ResponseEntity.ok("TelegramUserId: " + telegramUserIdString);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // --------------------- Get Telegram User Type id by Telegram User Id ---------------------
    // /telegramuser/usertypeid/1
    @GetMapping(value =  "telegramuser/usertypeid/{TelegramUserId}")
    public ResponseEntity<String> findUserTypeId(@PathVariable Long telegramUserId){
        try{
            Long userTypeIdLong = TelegramUserService.findUserTypeId(telegramUserId);
            String userTypeIdString = Long.toString(userTypeIdLong);
            return ResponseEntity.ok("UserTypeId: " + userTypeIdString);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // --------------------- Get Telegram User Name by Telegram User Id ---------------------
    // /telegramuser/telegramname/1
    @GetMapping(value = "telegramuser/telegramname/{TelegramUserId}")
    public ResponseEntity<String> findTelegramNameByTelegramUserId(Long id){
        try{
            return ResponseEntity.ok(TelegramUserService.findTelegramNameByTelegramUserId(id));
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
	}
}
