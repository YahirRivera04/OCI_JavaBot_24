package com.springboot.MyTodoList.controller;
import com.springboot.MyTodoList.model.Conversation;
import com.springboot.MyTodoList.service.ConversationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class ConversationController {
    @Autowired
    private ConversationService ConversationService;
    //@CrossOrigin

    // ## Get ##
    @GetMapping(value = "/conversation/{id}")
    public ResponseEntity<Conversation> getItemById(@PathVariable int id){
        try{
            ResponseEntity<Conversation> responseEntity = ConversationService.getItemById(id);
            return new ResponseEntity<Conversation>(responseEntity.getBody(), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // ## Update ##
    @PutMapping(value = "conversation/{id}")
    public ResponseEntity<Conversation> updateConversation(@PathVariable int id, @RequestBody Conversation td){
        try{
            Conversation conversationItem = ConversationService.updateConversation(id, td);
            System.out.println(conversationItem.toString());
            return new ResponseEntity<>(conversationItem,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    } 
}
