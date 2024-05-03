package com.springboot.MyTodoList.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

import com.springboot.MyTodoList.model.BotOption;
import com.springboot.MyTodoList.repository.BotOptionRepository;

// Marks the class as a Spring service component, 
// allowing it to be automatically detected and instantiated by Spring container
// during component scanning.

@Service
public class BotOptionService {
    @Autowired
    private BotOptionRepository repository;
    
    // --------------------- Read Method ---------------------

    public ResponseEntity<BotOption> getItemById(int id){
        Optional<BotOption> data = repository.findById(id);
        if (data.isPresent()){
            return new ResponseEntity<>(data.get(), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // --------------------- Update Method ---------------------

    public BotOption updateBotOption(int id, BotOption td) {
        Optional<BotOption> data = repository.findById(id);
        if(data.isPresent()){
            BotOption botOption = data.get();
            botOption.setID(id);
            botOption.setText(td.getText());
            botOption.setDescription(td.getDescription());
            return repository.save(botOption);
        }else{
            return null;
        }
    }

}
