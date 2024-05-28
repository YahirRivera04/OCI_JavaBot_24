package com.springboot.MyTodoList.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.test.context.jdbc.Sql;

import static org.mockito.Mockito.timeout;

import java.util.List;
import java.util.Optional;
import com.springboot.MyTodoList.model.TelegramUser;
import com.springboot.MyTodoList.model.ToDoItem;
import com.springboot.MyTodoList.model.UserType;
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

    // --------------------- Get All User Info Method ---------------------
    public TelegramUser getUserInfo(Long telegramUserId) {
        return telegramUserRepository.findById(telegramUserId).orElse(null);
    }

    
    // --------------------- Find Chat Id by Telegram User Method ---------------------
    public Boolean existByChatId(Long chatId){
        return telegramUserRepository.existsByChatId(chatId);
    }

    // --------------------- Get Telegram User ID by Telegram User name Method  ---------------------
    public Long findTelegramUserId(String TelegramName){
        return telegramUserRepository.findTelegramUserIdByTelegramName(TelegramName);
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

    // --------------------- Get Telegram User Id by Chat Id Method  ---------------------
    public Long findUserIdByChatId(Long chatId){
        return telegramUserRepository.findUserIdByChatId(chatId);
    }


    // --------------------- Get User Type id by User Id ---------------------
    public Long findUserTypeId(Long userId){
        return telegramUserRepository.findUserTypeId(userId);
    }


}