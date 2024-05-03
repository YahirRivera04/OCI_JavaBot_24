package com.springboot.MyTodoList.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import com.springboot.MyTodoList.model.SprintUpdate;
import com.springboot.MyTodoList.repository.SprintUpdateRepository;

// Marks the class as a Spring service component, 
// allowing it to be automatically detected and instantiated by Spring container
// during component scanning.

@Service
public class SprintUpdateService {
    @Autowired
    private SprintUpdateRepository repository;
    
    // --------------------- Read Method ---------------------

    public ResponseEntity<SprintUpdate> getItemById(int id){
        Optional<SprintUpdate> data = repository.findById(id);
        if (data.isPresent()){
            return new ResponseEntity<>(data.get(), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // --------------------- Update Method ---------------------

    public SprintUpdate updateTaskUpdate(int id, SprintUpdate td) {
        Optional<SprintUpdate> data = repository.findById(id);
        if(data.isPresent()){
            SprintUpdate sprintUpdate = data.get();
            // sprintUpdate.setID(id);
            // sprintUpdate.setCreation_ts(td.getCreation_ts());
            // sprintUpdate.setDescription(td.getDescription());
            // sprintUpdate.setDone(td.isDone());
            return repository.save(sprintUpdate);
        }else{
            return null;
        }
    }

}
