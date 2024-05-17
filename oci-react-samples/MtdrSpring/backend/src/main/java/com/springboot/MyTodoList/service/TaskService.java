package com.springboot.MyTodoList.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import com.springboot.MyTodoList.model.Task;
import com.springboot.MyTodoList.repository.TaskRepository;

// Marks the class as a Spring service component, 
// allowing it to be automatically detected and instantiated by Spring container
// during component scanning.

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;
    
    public List<Task> findAll(){
        List<Task> tasksItems = taskRepository.findAll();
        return tasksItems;
    }

    // --------------------- Read Method ---------------------

    public ResponseEntity<Task> getItemById(int id){
        Optional<Task> data = taskRepository.findById(id);
        if (data.isPresent()){
            return new ResponseEntity<>(data.get(), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public List<String> findTaskList(int TelegramId){
        return taskRepository.findByTelegramUserId(TelegramId);
    }

    // --------------------- Update Method ---------------------

    public Task updateTask(int id, Task td) {
        Optional<Task> data = taskRepository.findById(id);
        if(data.isPresent()){
            Task task = data.get();
            // task.setID(id);
            // task.setCreation_ts(td.getCreation_ts());
            // task.setDescription(td.getDescription());
            // task.setDone(td.isDone());
            return taskRepository.save(task);
        }else{
            return null;
        }
    }
    
    // --------------------- Create Method ---------------------

    public Task addTask(Task task){
        return taskRepository.save(task);
    }

    // --------------------- Delete Method ---------------------

    public boolean deleteTask(int id){
        try{
            taskRepository.deleteById(id);
            return true;
        }catch(Exception e){
            return false;
        }
    }

}
