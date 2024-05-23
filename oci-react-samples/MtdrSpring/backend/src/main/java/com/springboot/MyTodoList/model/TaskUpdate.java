package com.springboot.MyTodoList.model;


import java.time.OffsetDateTime;

import javax.persistence.*;
@Entity
@Table(name = "TASKUPDATE")
public class TaskUpdate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
    @Column(name = "TASKUPDATEID")
    Long taskUpdateId;
    
    @Column(name = "TIMESTAMP")
    OffsetDateTime timeStamp;
    
    @ManyToOne
    @JoinColumn(name = "UPDATETYPEID")
    UpdateType updateTypeId;
    
    @ManyToOne
    @JoinColumn(name = "TASKID")
    Task taskId;
    
    @ManyToOne
    @JoinColumn(name = "TELEGRAMUSERID")
    TelegramUser telegramUserId;

    public TaskUpdate(){

    }
    public TaskUpdate(Long ID, OffsetDateTime timeStamp, UpdateType updateType, Task task, TelegramUser telegramUser) {
        this.taskUpdateId = ID;
        this.timeStamp = timeStamp;
        this.updateTypeId = updateType;
        this.taskId = task;
        this.telegramUserId = telegramUser;
    }

    public Long getID() {
        return taskUpdateId;
    }

    public void setID(Long ID) {
        this.taskUpdateId = ID;
    }

    public OffsetDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(OffsetDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public UpdateType getUpdateType() {
        return updateTypeId;
    }

    public void setUpdateType(UpdateType updateType) {
        this.updateTypeId = updateType;
    }

    public Task getTask() {
        return taskId;
    }

    public void setTask(Task task) {
        this.taskId = task;
    }

    public TelegramUser getTelegramUser() {
        return telegramUserId;
    }

    public void setTelegramUser(TelegramUser telegramUser) {
        this.telegramUserId = telegramUser;
    }


    @Override
    public String toString() {
        return "ToDoItem{" +
                "ID=" + taskUpdateId +
                ", TIMESTAMP=" + timeStamp +
                ", Update Type=" + updateTypeId.getName() +
                ", Task=" + taskId.getName() +
                ", Telegram User=" + telegramUserId.getName() +
                '}';
    }
}
