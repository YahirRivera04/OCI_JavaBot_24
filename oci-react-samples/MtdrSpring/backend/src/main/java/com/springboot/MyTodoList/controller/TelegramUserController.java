package com.springboot.MyTodoList.controller;
import com.springboot.MyTodoList.model.TelegramUser;
import com.springboot.MyTodoList.service.TelegramUserService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class TelegramUserController {
    @Autowired
    private TelegramUserService telegramUserService;

    // ##################### Telegram User Controller Metods ##################### //

    @GetMapping(value = "/telegramuser/")
    public ResponseEntity<String> findTelegramUsers(){
        String info = ""; 
        try{
            List<TelegramUser> telegramUserList = telegramUserService.findAllTelegramUsers();
            for(int i = 0; i < telegramUserList.size(); i++){
                info += telegramUserList.get(i).toString() + " \n";
            }
            return ResponseEntity.ok(info);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // --------------------- Update ChatId Method ---------------------
    //@CrossOrigin
    @PutMapping(value = "telegramuser/setchatid/{TelegramUserId}")
    public ResponseEntity<String> updateTelegramUserController( @PathVariable Long telegramUserId, @RequestBody Long chatId){
        try {
            return ResponseEntity.ok(telegramUserService.updateChatId(telegramUserId, chatId));
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    // ##################### Bot Controller Metods ##################### //

    // Get All Telegram Users
    public ResponseEntity<List<TelegramUser>> findAllTelegramUsers(){
        List<TelegramUser> telegramUserList = telegramUserService.findAllTelegramUsers();
        return ResponseEntity.ok(telegramUserList);
    }
    
    // Put Telegram User ChatId with Id
    public ResponseEntity<String> updateChatId(Long id, Long chatId){
        return ResponseEntity.ok(telegramUserService.updateChatId(id, chatId));
    }

    public String printTelegramUserList(TelegramUser telegramUser){
        // Print all information form user type
        String telegramUserInfo = "Id " + telegramUser.getID().toString() + 
        " \nName " + telegramUser.getName() + 
        " \nEmail " + telegramUser.getEmail() + 
        " \nPhone Number " + telegramUser.getPhoneNumber() + 
        " \nTelegram Name " + telegramUser.getTelegramName() + 
        " \nChat Id " + telegramUser.getChatId() + 
        " \nUser Type Id" + telegramUser.getUserType().getID() +
        " \nUser Type Name " + telegramUser.getUserType().getName() + 
        " \nUser Type Description " + telegramUser.getUserType().getDescription();


        return telegramUserInfo;
    }


}
