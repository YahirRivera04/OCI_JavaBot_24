package com.springboot.MyTodoList.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.Optional;

import com.springboot.MyTodoList.model.TelegramUser;
import com.springboot.MyTodoList.repository.TelegramUserRepository;

// Marks the class as a Spring service component, 
// allowing it to be automatically detected and instantiated by Spring container
// during component scanning.

@Service
public class TelegramUserService {
    @Autowired
    private TelegramUserRepository repository;
    
    // --------------------- Read Method ---------------------

    public ResponseEntity<TelegramUser> getItemById(int id){
        Optional<TelegramUser> data = repository.findById(id);
        if (data.isPresent()){
            return new ResponseEntity<>(data.get(), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // --------------------- Update Method ---------------------

    public TelegramUser updateTelegramUser(int id, TelegramUser td) {
        Optional<TelegramUser> data = repository.findById(id);
        if(data.isPresent()){
            TelegramUser telegramUser = data.get();
            // telegramUser.setID(id);
            // telegramUser.setCreation_ts(td.getCreation_ts());
            // telegramUser.setDescription(td.getDescription());
            // telegramUser.setDone(td.isDone());
            return repository.save(telegramUser);
        }else{
            return null;
        }
    }

}
