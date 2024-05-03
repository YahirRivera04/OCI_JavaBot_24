package com.springboot.MyTodoList.model;


import java.util.List;

import javax.persistence.*;
@Entity
@Table(name = "TASK")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TaskId")
    int ID;
    @Column(name = "Name")
    String name;
    @Column(name = "Description")
    String description;
    @Column(name = "EstimatedHours")
    float estimatedHours;
    @Column(name = "Priority")
    int priority;
    @ManyToOne
    @JoinColumn(name = "TelegramUserId")
    TelegramUser telegramUser;
    @ManyToOne
    @JoinColumn(name = "SprintId")
    Sprint sprint;
    @ManyToOne
    @JoinColumn(name = "TaskStatusId")
    TaskStatus taskStatus;
    @OneToMany(mappedBy = "TaskId", cascade = CascadeType.ALL)
    List<TaskUpdate> taskUpdates;

    public Task(){

    }
    public Task(int ID, String name, String description, float estimatedHours, int priority, TelegramUser telegramUser, Sprint sprint, TaskStatus taskStatus) {
        this.ID = ID;
        this.name = name;
        this.description = description;
        this.estimatedHours = estimatedHours;
        this.priority = priority;
        this.telegramUser = telegramUser;
        this.sprint = sprint;
        this.taskStatus = taskStatus;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
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
        return telegramUser;
    }

    public void setTelegramUser(TelegramUser telegramUser) {
        this.telegramUser = telegramUser;
    }

    public Sprint getSprint() {
        return sprint;
    }

    public void setSprint(Sprint sprint) {
        this.sprint = sprint;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public List<TaskUpdate> geTaskUpdates(){
        return taskUpdates;
    }

    @Override
    public String toString() {
        return "ToDoItem{" +
                "ID=" + ID +
                ", Name='" + name + '\'' +
                ", Description=" + description +
                ", Estimated Hours=" + estimatedHours +
                ", Priority=" + priority +
                ", Telegram User=" + telegramUser.getName() +
                ", Sprint=" + sprint.getName() +
                ", Task Status=" + taskStatus.getName() +
                '}';
    }
}