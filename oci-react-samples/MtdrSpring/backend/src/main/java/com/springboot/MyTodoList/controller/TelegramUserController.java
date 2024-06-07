package com.springboot.MyTodoList.controller;
import com.springboot.MyTodoList.model.TelegramUser;
import com.springboot.MyTodoList.service.TelegramUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/telegramuser")
public class TelegramUserController {
    @Autowired
    private TelegramUserService telegramUserService;

    // --------------------- Exists by Chat Id Method ---------------------
    @GetMapping(value = "/existuser/{chatId}/{telegramName}")
    public ResponseEntity<String> findByTelegramName(@PathVariable Long chatId, @PathVariable String telegramName){
        String info = ""; 
        try{
            Boolean exist = telegramUserService.existsByChatId(chatId);
            if(exist){
                TelegramUser telegramUser = telegramUserService.findByTelegramName(telegramName);
                info = "User " + telegramUser.getName() + "exist in system";
            }
            return new ResponseEntity<String>(info, HttpStatus.FOUND);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // --------------------- Get All Method ---------------------
    @GetMapping(value = "/alltelegramuser/")
    public ResponseEntity<String> findAllTelegramUsers(){
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

    // --------------------- Get TelegramUser by ChatId Method ---------------------
    @GetMapping(value = "/{telegarmName}")
    public ResponseEntity<String> findByTelegramName(@PathVariable String telegramName){
        try{
            TelegramUser telegramUser = telegramUserService.findByTelegramName(telegramName);
            
            if(telegramUser != null){
                return ResponseEntity.ok(telegramUserService.printTelegramUserList(telegramUser));
            }
            else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
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

}
