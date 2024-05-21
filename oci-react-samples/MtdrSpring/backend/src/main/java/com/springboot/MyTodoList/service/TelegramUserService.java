package com.springboot.MyTodoList.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static org.mockito.Mockito.timeout;

import java.util.List;
import java.util.Optional;

import com.springboot.MyTodoList.model.TelegramUser;
import com.springboot.MyTodoList.model.ToDoItem;
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

    // --------------------- Check Table Exists Method ---------------------
    public String checkIfTableExists(){
        try {
            telegramUserRepository.findTable();
            if(telegramUserRepository.findTable().isEmpty()){
                return "Table does not exist";
            }
            return "TelegramUser Table Exists and is accesible";
        }
        catch (Exception e){
            return "Table does not exist" + e.getMessage();
        }
    }
    
    // --------------------- Read by Telegram User Method ---------------------

    public Boolean existsByTelegramName(String TelegramName){
        return telegramUserRepository.existsByTelegramName(TelegramName);
    }

    // --------------------- Get Telegram ID by Telegram User name  ---------------------

    public Long findTelegramUserId(String TelegramName){
        return telegramUserRepository.findTelegramUserIdByTelegramName(TelegramName);
    }

    // --------------------- Get Caht ID by Telegram User Id  ---------------------

    public Long fndChatIdByTelegramUserId(Long id){
        return telegramUserRepository.fndChatIdByTelegramUserId(id);
    }

    // --------------------- Update ChatId Method ---------------------

    public String updateChatId(Long id, Long chatId) {
        TelegramUser telegramUser = new TelegramUser();
        try{
            telegramUser.setChatId(chatId);
            telegramUser.setID(id);
            telegramUserRepository.save(telegramUser);

            return "ChatId updated successfully ";
        }
        catch (Exception e){
            return "ChatId update failed";
        }
    }
}
