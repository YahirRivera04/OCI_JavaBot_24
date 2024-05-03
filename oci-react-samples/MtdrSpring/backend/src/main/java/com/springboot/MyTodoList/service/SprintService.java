package com.springboot.MyTodoList.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import com.springboot.MyTodoList.model.Sprint;
import com.springboot.MyTodoList.repository.SprintRepository;

// Marks the class as a Spring service component, 
// allowing it to be automatically detected and instantiated by Spring container
// during component scanning.

@Service
public class SprintService {
    @Autowired
    private SprintRepository sprintRepository;
    
    // --------------------- Read Method ---------------------

    public ResponseEntity<Sprint> getItemById(int id){
        Optional<Sprint> data = sprintRepository.findById(id);
        if (data.isPresent()){
            return new ResponseEntity<>(data.get(), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // --------------------- Update Method ---------------------

    public Sprint updateSprint(int id, Sprint td) {
        Optional<Sprint> data = sprintRepository.findById(id);
        if(data.isPresent()){
            Sprint sprint = data.get();
            // sprint.setID(id);
            // sprint.setCreation_ts(td.getCreation_ts());
            // sprint.setDescription(td.getDescription());
            // sprint.setDone(td.isDone());
            return sprintRepository.save(sprint);
        }else{
            return null;
        }
    }

}
