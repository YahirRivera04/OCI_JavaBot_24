package com.springboot.MyTodoList.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import com.springboot.MyTodoList.model.Task;
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

    // Post Task
    public String createTask(Task task){
        try{
            taskRepository.save(task);
            return "Task " + task.getName() + " created succesfully.";
        }
        catch(Exception e){
            return "Task with Id " + task.getID() + " fail.\nERROR: " + e.toString();
        }
    }
    
    // Delete Task
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
}
