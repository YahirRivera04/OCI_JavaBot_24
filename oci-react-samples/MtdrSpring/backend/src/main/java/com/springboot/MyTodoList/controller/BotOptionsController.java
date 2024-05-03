package com.springboot.MyTodoList.controller;
import com.springboot.MyTodoList.model.BotOption;
import com.springboot.MyTodoList.service.BotOptionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class BotOptionsController {
    @Autowired
    private BotOptionService botOptionService;
    //@CrossOrigin

    // ## Get ##
    @GetMapping(value = "/botoption/{id}")
    public ResponseEntity<BotOption> getItemById(@PathVariable int id){
        try{
            ResponseEntity<BotOption> responseEntity = botOptionService.getItemById(id);
            return new ResponseEntity<BotOption>(responseEntity.getBody(), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // ## Update ##
    @PutMapping(value = "botoption/{id}")
    public ResponseEntity updateBotOption(@PathVariable int id, @RequestBody BotOption td){
        try{
            BotOption botOptionItem = botOptionService.updateBotOption(id, td);
            System.out.println(botOptionItem.toString());
            return new ResponseEntity<>(botOptionItem,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
}
