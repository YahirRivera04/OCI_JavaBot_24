package com.springboot.MyTodoList.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.HashMap;
import java.util.Optional;

import com.springboot.MyTodoList.model.Task;
import com.springboot.MyTodoList.model.TelegramUser;
import com.springboot.MyTodoList.model.TaskUpdate;
import com.springboot.MyTodoList.repository.TaskRepository;
import com.springboot.MyTodoList.repository.TaskUpdateRepository;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskUpdateRepository taskUpdateRepository;
    
    public TaskService(TaskRepository taskRepository){
        this.taskRepository = taskRepository;
    }

    // Get All Tasks By Telegram User Id
    public List<Task> findAllTaskByTelegramUserId(Long telegramUserId){
        return taskRepository.findAllTasksByTelegramUserId(telegramUserId);
    }

    // Get All Tasks
    public List<Task> findAllTask(){
        return taskRepository.findAll();
    }

    // CREATE TASK METHOD
    public String createTask(Task task){
        try{
            taskRepository.save(task);
            return "Task " + task.getName() + " created succesfully.";
        }
        catch(Exception e){
            return "Task with Id " + task.getID() + " fail.\nERROR: " + e.toString();
        }
    }
    
    // DELETE TASK METHOD
    public String deleteTask(Long telegramUserId, String name, Long taskId){
        try{
            taskUpdateRepository.deleteTasksUpdateByTaskId(taskId);
            taskRepository.deleteTasksByTelegramUserIdAndTaskName(telegramUserId, name);
            return "Task deleted successfully";
        }
        catch(Exception e){
            return "Task fail to delete " + e.toString();
        }
        
    }


    // EDIT TASK METHOD
    public String editTask(String messageTextFromTelegram, HashMap<Long, Task> taskList, TelegramUser telegramUser){

        // Retrieve data from user response
        String[] editTaskData = messageTextFromTelegram.split("\n");
        // New Task Instance
        Task editTask = new Task();
        // New Task Update Instance
        TaskUpdate editTaskUpdate = new TaskUpdate();
        // Auxiliar varibale
        int taskId = -1;

        // Set Id
        for(int i = 0; i < taskList.size(); i++){
            if(taskList.get(i).getID().toString().equals(editTaskData[0].substring(3, editTaskData[0].length()).trim())){
                editTask.setID(taskList.get(i).getID());
                taskId = i;
            }
        }

        // Set Name
        editTask.setName(Optional.of(editTaskData[1].substring(6, editTaskData[1].length()).trim())
        .filter(s -> !s.isEmpty())
        .orElse(taskList.get(taskId).getName()));

        // Set Description
        editTask.setDescription(Optional.of(editTaskData[2].substring(13, editTaskData[2].length()).trim())
        .filter(s -> !s.isEmpty())
        .orElse(taskList.get(taskId).getDescription()));

        // Estimated Hours
        String estimatedHourData = editTaskData[3].substring(16, editTaskData[3].length()).trim();
        if (estimatedHourData.isEmpty()) {
            estimatedHourData = String.valueOf(taskList.get(taskId).getEstimatedHours());
        }
        editTask.setEstimatedHours(Float.parseFloat(estimatedHourData));

        // Set Priority
        String priorityData = editTaskData[4].substring(16, editTaskData[4].length()).trim();
        if(priorityData.isEmpty()){
            priorityData = String.valueOf(taskList.get(taskId).getPriority());
        }
        editTask.setPriority(Integer.parseInt(priorityData));
        
        // Set Telegram User
        editTask.setTelegramUser(telegramUser);

        // Set Sprint Id by name
        String sprintName = editTaskData[5].substring(13, editTaskData[5].length()).trim();
        if(sprintName.isEmpty()){
            sprintName = String.valueOf(taskList.get(taskId).getSprint().getName());
        }
        for(int i = 0; i < sprintList.size(); i++){
            if(sprintList.get(i).getName().equals(sprintName)){
                editTask.setSprint(sprintList.get(i));
                break;
            }
        }

        // Set Task Status
        String taskStatusName = editTaskData[6].substring(12, editTaskData[6].length()).trim();
        if(taskStatusName.isEmpty()){
            taskStatusName = String.valueOf(taskList.get(taskId).getTaskStatus().getName());
        }
        for(int i = 0; i < taskStatusList.size(); i++){
            if(taskStatusList.get(i).getName().equals(taskStatusName)){
                editTask.setTaskStatus(taskStatusList.get(i));
                break;
            }
        }

        // // Set Task Update with Update Type
        // String updateTypeName = editTaskData[7].substring(15, editTaskData[7].length()).trim();
        // for(int i = 0; i < updateTypeList.size(); i++){
        //     if(updateTypeList.get(i).getName().equals(updateTypeName)){
        //         Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
        //         // Task Update
        //         editTaskUpdate.setTimeStamp(timeStamp);
        //         editTaskUpdate.setUpdateType(updateTypeList.get(i));
        //         editTaskUpdate.setTask(editTask);
        //         editTaskUpdate.setTelegramUser(telegramUser);
        //         break;
        //     }
        // }

        // Update Task to Data Base
        ResponseEntity<String> taskUpdateResponse = taskController.createNewTask(editTask);
       
        // Update Task Update to Data Base 
        //taskUpdateController.createTaskUpdate(editTaskUpdate);

        return taskUpdateResponse.getBody();
    }

    // Print Task
    public String printTask(Task task){
        String taskInfo = "Task Id " + task.getID() +
                " \nName " + task.getName() + 
                " \nDescription " + task.getDescription() + 
                " \nEstimated Hours " + task.getEstimatedHours() + 
                " \nPriority " + task.getPriority() + 
                " \nTelegram User Name " + task.getTelegramUser().getName() + 
                " \nSprint Name " + task.getSprint().getName() + 
                " \nTask Status " + task.getTaskStatus().getName();

        return taskInfo;
    }

}
