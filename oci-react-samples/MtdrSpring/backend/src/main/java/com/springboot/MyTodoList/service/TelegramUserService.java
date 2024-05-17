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

    public int findTelegramUserId(String TelegramName){
        return telegramUserRepository.findTelegramUserIdByTelegramName(TelegramName);
    }

    // --------------------- Get Telegram User Info Method ---------------------

    // public TelegramUser getTelegramUserInfo(String TelegramName){
    //     return telegramUserRepository.findByTelegramNameIs(TelegramName);
    // }

    // --------------------- Update ChatId Method ---------------------

    public TelegramUser updateTelegramUser(Long id, TelegramUser telegramUser) {
        // Get all the data by id
        Optional<TelegramUser> telegramUserData = telegramUserRepository.findById(id);
        // If data exists then update the chatIds
        if(telegramUserData.isPresent()){
            TelegramUser User = telegramUserData.get();
            User.setChatId(telegramUser.getChatId());
            return telegramUserRepository.save(User);

        }else{

            return null;
        }
    }


    // // --------------------- Read Method ---------------------

    // public ResponseEntity<TelegramUser> getItemById(int id){
    //     Optional<TelegramUser> data = telegramUserRepository.findById(id);
    //     if (data.isPresent()){
    //         return new ResponseEntity<>(data.get(), HttpStatus.OK);
    //     }else{
    //         return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    //     }
    // }

    // // --------------------- Update Method ---------------------

    // public TelegramUser updateTelegramUser(int id, TelegramUser td) {
    //     Optional<TelegramUser> data = telegramUserRepository.findById(id);
    //     if(data.isPresent()){
    //         TelegramUser telegramUser = data.get();
    //         // telegramUser.setID(id);
    //         // telegramUser.setCreation_ts(td.getCreation_ts());
    //         // telegramUser.setDescription(td.getDescription());
    //         // telegramUser.setDone(td.isDone());
    //         return telegramUserRepository.save(telegramUser);
    //     }else{
    //         return null;
    //     }
    // }

}
