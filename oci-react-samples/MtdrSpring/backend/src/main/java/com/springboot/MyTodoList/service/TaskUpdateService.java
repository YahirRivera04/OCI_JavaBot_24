package com.springboot.MyTodoList.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import com.springboot.MyTodoList.model.TaskUpdate;
import com.springboot.MyTodoList.repository.TaskUpdateRepository;

// Marks the class as a Spring service component, 
// allowing it to be automatically detected and instantiated by Spring container
// during component scanning.

@Service
public class TaskUpdateService {
    @Autowired
    private TaskUpdateRepository repository;
    
    // --------------------- Read Method ---------------------

    public ResponseEntity<TaskUpdate> getItemById(int id){
        Optional<TaskUpdate> data = repository.findById(id);
        if (data.isPresent()){
            return new ResponseEntity<>(data.get(), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // --------------------- Update Method ---------------------

    public TaskUpdate updateTaskUpdate(int id, TaskUpdate td) {
        Optional<TaskUpdate> data = repository.findById(id);
        if(data.isPresent()){
            TaskUpdate taskUpdate = data.get();
            // taskUpdate.setID(id);
            // taskUpdate.setCreation_ts(td.getCreation_ts());
            // taskUpdate.setDescription(td.getDescription());
            // taskUpdate.setDone(td.isDone());
            return repository.save(taskUpdate);
        }else{
            return null;
        }
    }

}
