package com.springboot.MyTodoList.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

import com.springboot.MyTodoList.model.Message;
import com.springboot.MyTodoList.repository.MessageRepository;

// Marks the class as a Spring service component, 
// allowing it to be automatically detected and instantiated by Spring container
// during component scanning.

@Service
public class MessageService {
    @Autowired
    private MessageRepository repository;
    
    // --------------------- Read Method ---------------------

    public ResponseEntity<Message> getItemById(int id){
        Optional<Message> data = repository.findById(id);
        if (data.isPresent()){
            return new ResponseEntity<>(data.get(), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // --------------------- Update Method ---------------------

    public Message updateMessage(int id, Message td) {
        Optional<Message> data = repository.findById(id);
        if(data.isPresent()){
            Message message = data.get();
            message.setID(id);
            message.setContent(td.getContent());
            message.setTelegramUser(td.getTelegramUser());
            message.setConversation(td.getConversation());
            return repository.save(message);
        }else{
            return null;
        }
    }

}
