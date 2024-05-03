package com.springboot.MyTodoList.controller;
import com.springboot.MyTodoList.model.Message;
import com.springboot.MyTodoList.service.MessageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class MessageController {
    @Autowired
    private MessageService messageService;
    //@CrossOrigin

    // ## Get ##
    @GetMapping(value = "/message/{id}")
    public ResponseEntity<Message> getItemById(@PathVariable int id){
        try{
            ResponseEntity<Message> responseEntity = messageService.getItemById(id);
            return new ResponseEntity<Message>(responseEntity.getBody(), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // ## Update ##
    @PutMapping(value = "message/{id}")
    public ResponseEntity updateMessage(@PathVariable int id, @RequestBody Message td){
        try{
            Message messageItem = messageService.updateMessage(id, td);
            System.out.println(messageItem.toString());
            return new ResponseEntity<>(messageItem,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
}
