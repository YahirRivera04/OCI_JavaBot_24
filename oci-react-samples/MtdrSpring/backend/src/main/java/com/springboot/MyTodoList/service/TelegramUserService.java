package com.springboot.MyTodoList.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import com.springboot.MyTodoList.model.TelegramUser;
import com.springboot.MyTodoList.repository.TelegramUserRepository;

// Marks the class as a Spring service component, 
// allowing it to be automatically detected and instantiated by Spring container
// during component scanning.

@Service
public class TelegramUserService {
    @Autowired
    private final TelegramUserRepository telegramUserRepository;

    public TelegramUserService(TelegramUserRepository telegramUserRepository) {
        this.telegramUserRepository = telegramUserRepository;
    }

    // --------------------- Exist By Chat Id Method ---------------------
    public Boolean existsByChatId(Long chatId){
        return telegramUserRepository.existsByChatId(chatId);
    }

    // --------------------- Find Telegram User By Telegram Name Method ---------------------
    public TelegramUser findByTelegramName(String telegramName){
        return telegramUserRepository.findByTelegramName(telegramName);
    }

    // --------------------- Find All Telegram Users Method ---------------------
    public List<TelegramUser> findAllTelegramUsers(){
        return telegramUserRepository.findAll();
    }


    // --------------------- Update ChatId Method ---------------------
    public String updateChatId(Long telegramUserId, Long chatId) {
        try{
            telegramUserRepository.setChatIdByTelegramUserId(telegramUserId, chatId);
            return "ChatId updated successfully ";
        }
        catch (Exception e){
            return "ChatId update failed " + e.getMessage();
        }
    }

   // --------------------- Print TelegramUser Method ---------------------
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