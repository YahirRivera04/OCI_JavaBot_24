package com.springboot.MyTodoList.controller;
import com.springboot.MyTodoList.model.BotOption;
import com.springboot.MyTodoList.service.BotOptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class BotOptionsController {
    @Autowired
    private BotOptionService botOptionService;
    // NO SE VAN A USAR
}
