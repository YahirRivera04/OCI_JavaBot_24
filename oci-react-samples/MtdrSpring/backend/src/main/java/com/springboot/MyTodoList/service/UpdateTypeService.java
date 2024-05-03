package com.springboot.MyTodoList.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import com.springboot.MyTodoList.model.UpdateType;
import com.springboot.MyTodoList.repository.UpdateTypeRepository;

// Marks the class as a Spring service component, 
// allowing it to be automatically detected and instantiated by Spring container
// during component scanning.

@Service
public class UpdateTypeService {
    @Autowired
    private UpdateTypeRepository UpdateTypeRepository;
    
    // --------------------- Read Method ---------------------

    public ResponseEntity<UpdateType> getItemById(int id){
        Optional<UpdateType> data = UpdateTypeRepository.findById(id);
        if (data.isPresent()){
            return new ResponseEntity<>(data.get(), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // --------------------- Update Method ---------------------

    public UpdateType updateTaskStatus(int id, UpdateType td) {
        Optional<UpdateType> data = UpdateTypeRepository.findById(id);
        if(data.isPresent()){
            UpdateType updateType = data.get();
            // updateType.setID(id);
            // updateType.setCreation_ts(td.getCreation_ts());
            // updateType.setDescription(td.getDescription());
            // updateType.setDone(td.isDone());
            return UpdateTypeRepository.save(updateType);
        }else{
            return null;
        }
    }

}
