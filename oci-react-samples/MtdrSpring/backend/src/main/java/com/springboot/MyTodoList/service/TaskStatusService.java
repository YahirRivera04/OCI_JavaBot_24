package com.springboot.MyTodoList.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import com.springboot.MyTodoList.model.TaskStatus;
import com.springboot.MyTodoList.repository.TaskStatusRepository;

// Marks the class as a Spring service component, 
// allowing it to be automatically detected and instantiated by Spring container
// during component scanning.

@Service
public class TaskStatusService {
    @Autowired
    private TaskStatusRepository taskStatusRepository;
    
    // --------------------- Get All Task Status Methods  ---------------------        
    public List<TaskStatus> findAllTaskStatus(){
        return taskStatusRepository.findAll();
    }

    // Print Task Status
	public String printTaskStatusList(TaskStatus taskStatus){
        String taskStatusInfo = /*"Id " + taskStatus.getID().toString()  + */ 
        " \nName " +  taskStatus.getName()/* +
        " \nDescription " + taskStatus.getDescription() */;
        return taskStatusInfo;
	}
}
