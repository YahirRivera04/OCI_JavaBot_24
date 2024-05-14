package com.springboot.MyTodoList.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import com.springboot.MyTodoList.model.Conversation;
import com.springboot.MyTodoList.repository.ConversationRepository;

// Marks the class as a Spring service component, 
// allowing it to be automatically detected and instantiated by Spring container
// during component scanning.

@Service
public class ConversationService {
    @Autowired
    private ConversationRepository ConversationRepository;
    
    // --------------------- Read Method ---------------------

    public ResponseEntity<Conversation> getItemById(int id){
        Optional<Conversation> data = ConversationRepository.findById(id);
        if (data.isPresent()){
            return new ResponseEntity<>(data.get(), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // --------------------- Update Method ---------------------

    public Conversation updateConversation(int id, Conversation td) {
        Optional<Conversation> data = ConversationRepository.findById(id);
        if(data.isPresent()){
            Conversation conversation = data.get();
            // conversation.setID(id);
            // conversation.setCreation_ts(td.getCreation_ts());
            // conversation.setDescription(td.getDescription());
            // conversation.setDone(td.isDone());
            return ConversationRepository.save(conversation);
        }else{
            return null;
        }
    }

    // --------------------- Create Method ---------------------
    public Conversation addConversation(Conversation conversation) {
        return ConversationRepository.save(conversation);
    }

}
