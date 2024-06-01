package com.springboot.MyTodoList.controller;
import com.springboot.MyTodoList.model.TelegramUser;
import com.springboot.MyTodoList.service.TelegramUserService;
import com.springboot.MyTodoList.model.UserType;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class TelegramUserController {
    @Autowired
    private TelegramUserService telegramUserService;

    @Autowired
    private UserTypeController userTypeController;

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

    // --------------------- Exist Chat Id by Chat Id Method ---------------------
    // /telegramuser/chatid/1984472475
    @GetMapping(value = "/telegramuser/chatid/{chatId}")
    public ResponseEntity<String> findChatIdByChatIdController(@PathVariable Long chatId){
        try{
            // Store variable to check if chatId exists
            Long chatIdLong = telegramUserService.findChatIdByChatId(chatId);
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
    public ResponseEntity<String> getTelegramUserIdController(@PathVariable String TelegramName){
        try{
            Long telegramUserIdLong = telegramUserService.findTelegramUserId(TelegramName);
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
    public ResponseEntity<String> updateTelegramUserController( @PathVariable Long telegramUserId, @RequestBody Long chatId){
        try {
            return ResponseEntity.ok(telegramUserService.updateChatId(telegramUserId, chatId));
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // --------------------- Get Telegram User Id by Chat Id Method  ---------------------
    // /telegramuser/telegramuserid/1984472475
    @GetMapping(value = "telegramuser/telegramuserid/{chatId}")
    public ResponseEntity<String> findUserIdByChatIdController(@PathVariable Long chatId){
        try{
            Long telegramUserIdLong = telegramUserService.findUserIdByChatId(chatId);
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
    public ResponseEntity<String> findUserTypeIdController(@PathVariable Long telegramUserId){
        try{
            Long userTypeIdLong = telegramUserService.findUserTypeId(telegramUserId);
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
    public ResponseEntity<String> findTelegramNameByTelegramUserIdController(Long id){
        try{
            return ResponseEntity.ok(telegramUserService.findTelegramNameByTelegramUserId(id));
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // ##################### Bot Controller Metods ##################### //


    public ResponseEntity<List<TelegramUser>> findAllTelegramUsers(){
        List<TelegramUser> telegramUserList = telegramUserService.findAllTelegramUsers();
        return ResponseEntity.ok(telegramUserList);
    }


    // Verify Telegram Chat Id from database
    public ResponseEntity<Long> findChatIdByChatId(Long chatId){
        Long response = telegramUserService.findChatIdByChatId(chatId);
        return ResponseEntity.ok(response);
    }

    // Get Telegram User Id with User Name
    public ResponseEntity<Long> getTelegramUserId(String TelegramName){
        return ResponseEntity.ok(telegramUserService.findTelegramUserId(TelegramName));
    }

    // Put Telegram User ChatId with Id
    public ResponseEntity<String> updateChatId(Long id, Long chatId){
        return ResponseEntity.ok(telegramUserService.updateChatId(id, chatId));
    }

    // Get Telegram User Id with Chat Id
    public ResponseEntity<Long> findUserId(Long chatId){
        return ResponseEntity.ok(telegramUserService.findUserIdByChatId(chatId));
    }

    // Get User Type with Id
    public ResponseEntity<Long> findUserTypeId(Long id){
        return ResponseEntity.ok(telegramUserService.findUserTypeId(id));
    }

    // Get Telegram User Name with Telegram User Id
    public ResponseEntity<String> findTelegramNameByTelegramUserId(Long id){
        return ResponseEntity.ok(telegramUserService.findTelegramNameByTelegramUserId(id));
    }

// Set all needed for Telegram User Local
	public TelegramUser setTelegramUser(Long chatId, String telegramUserName){

		// Add db informaton to the local user
		TelegramUser user = new TelegramUser();
		// Get User Type Information
		UserType dev = new UserType();
		UserType man = new UserType();
		
		dev = userTypeController.getUserTypeList().get(1);
		man = userTypeController.getUserTypeList().get(0);
		
		// Set Chat Id
		user.setChatId(chatId);		
		// Set Telegram User Id
		user.setID(findUserId(user.getChatId()).getBody());
		// Set User Type
		if(findUserTypeId(user.getID()).getBody() == dev.getID()) user.setUserType(dev);
		else user.setUserType(man);
		// Set Telegram User Name
		if(telegramUserName.equals("")) user.setTelegramName(findTelegramNameByTelegramUserId(user.getID()).getBody());
		else user.setTelegramName(telegramUserName);
		
		return user;
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
        " \nUser Type Description " + telegramUser.getUserType().getDescription()+
        " \nAsignado a " + telegramUser.getTeams().size() + " equipos" +
        " \nTeam Id " + telegramUser.getTeams().get(0).getID().toString() + 
        " \nTeam Name " + telegramUser.getTeams().get(0).getName() + 
        " \nTeam Description " + telegramUser.getTeams().get(0).getDescription();


        return telegramUserInfo;
    }


}
