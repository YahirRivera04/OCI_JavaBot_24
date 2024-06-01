package com.springboot.MyTodoList.controller;
import com.springboot.MyTodoList.model.BotMenu;
import com.springboot.MyTodoList.service.BotMenuService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class BotMenuController {
    @Autowired
    private BotMenuService botMenuService;
    
    // NO SE VAN A USAR

}
