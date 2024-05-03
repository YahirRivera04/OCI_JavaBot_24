package com.springboot.MyTodoList.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

import com.springboot.MyTodoList.model.BotMenu;
import com.springboot.MyTodoList.repository.BotMenuRepository;

// Marks the class as a Spring service component, 
// allowing it to be automatically detected and instantiated by Spring container
// during component scanning.

@Service
public class BotMenuService {
    @Autowired
    private BotMenuRepository repository;
    
    // --------------------- Read Method ---------------------

    public ResponseEntity<BotMenu> getItemById(int id){
        Optional<BotMenu> data = repository.findById(id);
        if (data.isPresent()){
            return new ResponseEntity<>(data.get(), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // --------------------- Update Method ---------------------

    public BotMenu updateBotMenu(int id, BotMenu td) {
        Optional<BotMenu> data = repository.findById(id);
        if(data.isPresent()){
            BotMenu updateBot = data.get();
            updateBot.setID(id);
            updateBot.setName(td.getName());
            updateBot.setDescription(td.getDescription());
            updateBot.setUserType(td.getUserType());
            return repository.save(updateBot);
        }else{
            return null;
        }
    }

}
