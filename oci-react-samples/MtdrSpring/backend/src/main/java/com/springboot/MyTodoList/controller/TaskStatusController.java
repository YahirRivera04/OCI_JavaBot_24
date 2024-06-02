package com.springboot.MyTodoList.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.MyTodoList.model.TaskStatus;
import com.springboot.MyTodoList.service.TaskStatusService;

import java.util.List;

@RestController
public class TaskStatusController {
    @Autowired
    private TaskStatusService taskStatusService;

    // ##################### Task Status Controller Metods ##################### //

    // --------------------- Get All Task Status ---------------------
    @GetMapping(value = "/taskstatus/")
    public ResponseEntity<String> findTaskStatus(){
        String info = "";
        try{
            List<TaskStatus> taskStatus = taskStatusService.findAllTaskStatus();
            for(int i = 0; i < taskStatus.size(); i++){
                info += taskStatus.get(i).toString() + "\n";
            }
            return ResponseEntity.ok(info);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // ##################### Bot Controller Metods ##################### //
    
    // Get all Task Status
	public ResponseEntity<List<TaskStatus>> findAllTaskStatus(){
		return ResponseEntity.ok(taskStatusService.findAllTaskStatus());
    } 
    
	// Print All Task Status
	public String printTaskStatusList(TaskStatus taskStatus){
        String taskStatusInfo = /*"Id " + taskStatus.getID().toString()  + */ 
        " \nName " +  taskStatus.getName()/* +
        " \nDescription " + taskStatus.getDescription() */;
        return taskStatusInfo;
	}
}
