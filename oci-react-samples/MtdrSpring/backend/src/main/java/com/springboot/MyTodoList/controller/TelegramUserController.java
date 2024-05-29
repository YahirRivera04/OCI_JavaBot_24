package com.springboot.MyTodoList.controller;
import com.springboot.MyTodoList.model.TelegramUser;
import com.springboot.MyTodoList.model.ToDoItem;
import com.springboot.MyTodoList.model.UserType;
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



    // --------------------- Exist Chat Id by Chat Id Method ---------------------
    @GetMapping(value = "/telegramuser/existsbychatid/{chatId}")
    public ResponseEntity<Boolean> existsByChatId(@PathVariable Long chatId){
        try {
            if(TelegramUserService.existsByChatId(chatId) != null){
                return ResponseEntity.ok(true);
            }
            else return ResponseEntity.ok(false);
        }    
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    // --------------------- Get Telegram User ID by Telegram User name Method  ---------------------
    @GetMapping(value = "/telegramuser/telegramuserid/{TelegramName}")
    public ResponseEntity<Long> getTelegramUserId(@PathVariable String TelegramName){
        return ResponseEntity.ok(TelegramUserService.findTelegramUserId(TelegramName));
    }
    
    // --------------------- Update ChatId Method ---------------------
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

    // --------------------- Get Telegram User Id by Chat Id Method  ---------------------
    @GetMapping(value = "telegramuser/telegramuserid/{ChatId}")
    public ResponseEntity<Long> findUserIdByChatId (@PathVariable Long chatId){
        return ResponseEntity.ok(TelegramUserService.findUserIdByChatId(chatId));
    }

    // --------------------- Get User Type id by User Id ---------------------
    @GetMapping(value =  "telegramuser/usertypeid/{TelegramUserId}")
    public ResponseEntity<Long> findUserTypeId(@PathVariable Long telegramUserId){
        return ResponseEntity.ok(TelegramUserService.findUserTypeId(telegramUserId));
    }

    // --------------------- Get Telegram User Name by Telegram User Id ---------------------
    @GetMapping(value = "telegramuser/telegramname/{TelegramUserId}")
    public ResponseEntity<String> findTelegramNameByTelegramUserId(Long id){
		return ResponseEntity.ok(TelegramUserService.findTelegramNameByTelegramUserId(id));
	}

}
