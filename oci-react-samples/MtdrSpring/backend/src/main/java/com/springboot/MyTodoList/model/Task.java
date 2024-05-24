package com.springboot.MyTodoList.model;


import java.util.List;

import javax.persistence.*;
@Entity
@Table(name = "TASK")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
    @Column(name = "TASKID")
    Long taskId;
    
    @Column(name = "NAME")
    String name;
    
    @Column(name = "DESCRIPTION")
    String description;
    
    @Column(name = "ESTIMATEDHOURS")
    float estimatedHours;
    
    @Column(name = "PRIORITY")
    int priority;
    
    @ManyToOne
    @JoinColumn(name = "TELEGRAMUSERID")
    TelegramUser telegramUserId;
    
    @ManyToOne
    @JoinColumn(name = "SPRINTID")
    Sprint sprintId;
    
    @ManyToOne
    @JoinColumn(name = "TASKSTATUSID")
    TaskStatus taskStatusId;
    
    @OneToMany(mappedBy = "taskUpdatesId", cascade = CascadeType.ALL)
    List<TaskUpdate> taskUpdatesId;

    public Task(){

    }
    public Task(Long ID, String name, String description, float estimatedHours, int priority, TelegramUser telegramUser, Sprint sprint, TaskStatus taskStatus) {
        this.taskId = ID;
        this.name = name;
        this.description = description;
        this.estimatedHours = estimatedHours;
        this.priority = priority;
        this.telegramUserId = telegramUser;
        this.sprintId = sprint;
        this.taskStatusId = taskStatus;
    }

    public Long getID() {
        return taskId;
    }

    public void setID(Long ID) {
        this.taskId = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getEstimatedHours() {
        return estimatedHours;
    }

    public void setEstimatedHours(float estimatedHours) {
        this.estimatedHours = estimatedHours;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public TelegramUser getTelegramUser() {
        return telegramUserId;
    }

    public void setTelegramUser(TelegramUser telegramUser) {
        this.telegramUserId = telegramUser;
    }

    public Sprint getSprint() {
        return sprintId;
    }

    public void setSprint(Sprint sprint) {
        this.sprintId = sprint;
    }

    public TaskStatus getTaskStatus() {
        return taskStatusId;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatusId = taskStatus;
    }

    public List<TaskUpdate> geTaskUpdates(){
        return taskUpdatesId;
    }

    @Override
    public String toString() {
        return "ToDoItem{" +
                "ID=" + taskId +
                ", Name='" + name + '\'' +
                ", Description=" + description +
                ", Estimated Hours=" + estimatedHours +
                ", Priority=" + priority +
                ", Telegram User=" + telegramUserId.getName() +
                ", Sprint=" + sprintId.getName() +
                ", Task Status=" + taskStatusId.getName() +
                '}';
    }
}
