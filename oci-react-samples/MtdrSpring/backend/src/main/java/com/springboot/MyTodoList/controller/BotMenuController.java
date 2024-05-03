package com.springboot.MyTodoList.controller;
import com.springboot.MyTodoList.model.BotMenu;
import com.springboot.MyTodoList.service.BotMenuService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class BotMenuController {
    @Autowired
    private BotMenuService botMenuService;
    //@CrossOrigin

    // ## Get ##
    @GetMapping(value = "/botmenu/{id}")
    public ResponseEntity<BotMenu> getItemById(@PathVariable int id){
        try{
            ResponseEntity<BotMenu> responseEntity = botMenuService.getItemById(id);
            return new ResponseEntity<BotMenu>(responseEntity.getBody(), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // ## Update ##
    @PutMapping(value = "botmenu/{id}")
    public ResponseEntity<BotMenu> updateBotMenu(@PathVariable int id, @RequestBody BotMenu td){
        try{
            BotMenu botMenuItem = botMenuService.updateBotMenu(id, td);
            System.out.println(botMenuItem.toString());
            return new ResponseEntity<>(botMenuItem,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
}
