package com.springboot.MyTodoList.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import com.springboot.MyTodoList.model.Conversation;
import com.springboot.MyTodoList.repository.ConversationRepository;

// Marks the class as a Spring service component, 
// allowing it to be automatically detected and instantiated by Spring container
// during component scanning.

@Service
public class ConversationService {
    @Autowired
    private ConversationRepository conversationRepository;
    
    // Push Start Conversation
    public Conversation pushConversationStart(){
        // Set variables
        Timestamp startTime = new Timestamp(System.currentTimeMillis());
        Conversation conversation = new Conversation();
        // Set data to object
        conversation.setStartTime(startTime);
        conversation.setEndTime(null);
        // Push to db
        Conversation newConversation = conversationRepository.save(conversation);
        // Get user
        return newConversation;
    }

    // Push End Conversation
    public void pushConversationEnd(Conversation conversation){
        if(conversation != null){
            Timestamp endTime = new Timestamp(System.currentTimeMillis());
            conversation.setEndTime(endTime);
            conversationRepository.updateConversation(conversation.getID(), conversation.getEndTime());
        }
    }

}
