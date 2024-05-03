package com.springboot.MyTodoList.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import com.springboot.MyTodoList.model.UserType;
import com.springboot.MyTodoList.repository.UserTypeRepository;

// Marks the class as a Spring service component, 
// allowing it to be automatically detected and instantiated by Spring container
// during component scanning.

@Service
public class UserTypeService {
    @Autowired
    private UserTypeRepository UserTypeRepository;
    
    // --------------------- Read Method ---------------------

    public ResponseEntity<UserType> getItemById(int id){
        Optional<UserType> data = UserTypeRepository.findById(id);
        if (data.isPresent()){
            return new ResponseEntity<>(data.get(), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // --------------------- Update Method ---------------------

    public UserType updateProject(int id, UserType td) {
        Optional<UserType> data = UserTypeRepository.findById(id);
        if(data.isPresent()){
            UserType project = data.get();
            // project.setID(id);
            // project.setCreation_ts(td.getCreation_ts());
            // project.setDescription(td.getDescription());
            // project.setDone(td.isDone());
            return UserTypeRepository.save(project);
        }else{
            return null;
        }
    }

}
