package com.springboot.MyTodoList.model;


import java.time.OffsetDateTime;

import javax.persistence.*;
@Entity
@Table(name = "SPRINTUPDATE")
public class SprintUpdate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SprintUpdateId")
    int ID;
    @Column(name = "TIMESTAMP")
    OffsetDateTime timeStamp;
    @ManyToOne
    @JoinColumn(name = "UpdateTypeId")
    UpdateType updateType;
    @ManyToOne
    @JoinColumn(name = "SprintId")
    Sprint sprint;
    @ManyToOne
    @JoinColumn(name = "TelegramUserId")
    TelegramUser telegramUser;

    public SprintUpdate(){

    }
    public SprintUpdate(int ID, OffsetDateTime timeStamp, UpdateType updateType, Sprint sprint, TelegramUser telegramUser) {
        this.ID = ID;
        this.timeStamp = timeStamp;
        this.updateType = updateType;
        this.sprint = sprint;
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

    public Sprint getSprint() {
        return sprint;
    }

    public void setSprint(Sprint sprint) {
        this.sprint = sprint;
    }

    public TelegramUser getTelegramUser() {
        return telegramUser;
    }

    public void setTelegramUser(TelegramUser telegramUser) {
        this.telegramUser = telegramUser;
    }


    @Override
    public String toString() {
        return "Sprint Update{" +
                "ID=" + ID +
                ", TIMESTAMP=" + timeStamp +
                ", Update Type=" + updateType.getName() +
                ", Sprint=" + sprint.getName() +
                ", Telegram User=" + telegramUser.getName() +
                '}';
    }
}
