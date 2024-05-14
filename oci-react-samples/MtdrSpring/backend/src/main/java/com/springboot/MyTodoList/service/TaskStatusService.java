package com.springboot.MyTodoList.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import com.springboot.MyTodoList.model.TaskStatus;
import com.springboot.MyTodoList.repository.TaskStatusRepository;

// Marks the class as a Spring service component, 
// allowing it to be automatically detected and instantiated by Spring container
// during component scanning.

@Service
public class TaskStatusService {
    @Autowired
    private TaskStatusRepository taskStatusRepository;
    
    // --------------------- Read Method ---------------------

    public ResponseEntity<TaskStatus> getItemById(int id){
        Optional<TaskStatus> data = taskStatusRepository.findById(id);
        if (data.isPresent()){
            return new ResponseEntity<>(data.get(), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // --------------------- Update Method ---------------------

    public TaskStatus updateTaskStatus(int id, TaskStatus td) {
        Optional<TaskStatus> data = taskStatusRepository.findById(id);
        if(data.isPresent()){
            TaskStatus taskStatus = data.get();
            // taskStatus.setID(id);
            // taskStatus.setCreation_ts(td.getCreation_ts());
            // taskStatus.setDescription(td.getDescription());
            // taskStatus.setDone(td.isDone());
            return taskStatusRepository.save(taskStatus);
        }else{
            return null;
        }
    }

    // --------------------- Create Method ---------------------    
    public TaskStatus addTaskStatus(TaskStatus taskStatus) {
        return taskStatusRepository.save(taskStatus);
    }

}
