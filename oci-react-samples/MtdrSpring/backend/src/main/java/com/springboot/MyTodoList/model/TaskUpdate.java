package com.springboot.MyTodoList.model;


import java.time.OffsetDateTime;

import javax.persistence.*;
@Entity
@Table(name = "TASKUPDATE")
public class TaskUpdate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TaskUpdateId")
    int ID;
    @Column(name = "TIMESTAMP")
    OffsetDateTime timeStamp;
    @ManyToOne
    @JoinColumn(name = "UpdateTypeId")
    UpdateType updateType;
    @ManyToOne
    @JoinColumn(name = "TaskId")
    Task task;
    @ManyToOne
    @JoinColumn(name = "TelegramUserId")
    TelegramUser telegramUser;

    public TaskUpdate(){

    }
    public TaskUpdate(int ID, OffsetDateTime timeStamp, UpdateType updateType, Task task, TelegramUser telegramUser) {
        this.ID = ID;
        this.timeStamp = timeStamp;
        this.updateType = updateType;
        this.task = task;
        this.telegramUser = telegramUser;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
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
                "ID=" + ID +
                ", TIMESTAMP=" + timeStamp +
                ", Update Type=" + updateType.getName() +
                ", Task=" + task.getName() +
                ", Telegram User=" + telegramUser.getName() +
                '}';
    }
}
