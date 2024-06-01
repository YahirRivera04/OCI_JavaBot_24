package com.springboot.MyTodoList.controller;
import com.springboot.MyTodoList.model.UpdateType;
import com.springboot.MyTodoList.service.UpdateTypeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class UpdateTypeController {
    @Autowired
    private UpdateTypeService UpdateTypeService;
    
}
