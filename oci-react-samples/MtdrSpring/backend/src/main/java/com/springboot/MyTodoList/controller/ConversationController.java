package com.springboot.MyTodoList.controller;
import com.springboot.MyTodoList.model.Conversation;
import com.springboot.MyTodoList.model.Project;
import com.springboot.MyTodoList.service.ConversationService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class ConversationController {
    @Autowired
    private ConversationService conversationService;

    // ##################### Conversation Controller Metods ##################### //

    // --------------------- Push Conversation Start ---------------------
    @GetMapping(value = "/conversation/")
    public ResponseEntity<String> createConversationStart(){
        String info = "";
        try{
            Conversation conversation = conversationService.pushConversationStart();
            info = "Conversation created with id " + conversation.getID();
            return ResponseEntity.ok(info);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

     // --------------------- Push Eonversation End ---------------------
     @GetMapping(value = "/conversation/{conversation}")
     public ResponseEntity<String> createConversationEnd(@PathVariable Conversation conversation){
         String info = "";
         try{
             conversationService.pushConversationEnd(conversation);
             info = "Conversation updated with id " + conversation.getID();
             return ResponseEntity.ok(info);
         }
         catch (Exception e){
             return new ResponseEntity<>(HttpStatus.NOT_FOUND);
         }
     }
 

}
