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
    @JoinColumn(name = "updateTypeId")
    UpdateType updateType;
    @ManyToOne
    @JoinColumn(name = "taskId")
    Task task;
    @ManyToOne
    @JoinColumn(name = "telegramUserId")
    TelegramUser telegramUser;

    public TaskUpdate(){

    }
    public TaskUpdate(Long ID, OffsetDateTime timeStamp, UpdateType updateType, Task task, TelegramUser telegramUser) {
        this.taskUpdateId = ID;
        this.timeStamp = timeStamp;
        this.updateType = updateType;
        this.task = task;
        this.telegramUser = telegramUser;
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
        return updateType;
    }

    public void setUpdateType(UpdateType updateType) {
        this.updateType = updateType;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public TelegramUser getTelegramUser() {
        return telegramUser;
    }

    public void setTelegramUser(TelegramUser telegramUser) {
        this.telegramUser = telegramUser;
    }


    @Override
    public String toString() {
        return "ToDoItem{" +
                "ID=" + taskUpdateId +
                ", TIMESTAMP=" + timeStamp +
                ", Update Type=" + updateType.getName() +
                ", Task=" + task.getName() +
                ", Telegram User=" + telegramUser.getName() +
                '}';
    }
}
