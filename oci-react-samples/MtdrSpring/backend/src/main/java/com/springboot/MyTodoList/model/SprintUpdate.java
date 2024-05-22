package com.springboot.MyTodoList.model;


import java.time.OffsetDateTime;

import javax.persistence.*;
@Entity
@Table(name = "SPRINTUPDATE")
public class SprintUpdate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SPRINTUPDATEID")
    Long sprintUpdateId;
    @Column(name = "TIMESTAMP")
    OffsetDateTime timeStamp;
    @ManyToOne
    @JoinColumn(name = "UPDATETYPEID")
    UpdateType updateType;
    @ManyToOne
    @JoinColumn(name = "SPRINTID")
    Sprint sprint;
    @ManyToOne
    @JoinColumn(name = "telegramUserId")
    TelegramUser telegramUser;
    public SprintUpdate(){

    }
    public SprintUpdate(Long ID, OffsetDateTime timeStamp, UpdateType updateType, Sprint sprint, TelegramUser telegramUser) {
        this.sprintUpdateId = ID;
        this.timeStamp = timeStamp;
        this.updateType = updateType;
        this.sprint = sprint;
        this.telegramUser = telegramUser;
    }

    public Long getID() {
        return sprintUpdateId;
    }

    public void setID(Long ID) {
        this.sprintUpdateId = ID;
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
                "ID=" + sprintUpdateId +
                ", TIMESTAMP=" + timeStamp +
                ", Update Type=" + updateType.getName() +
                ", Sprint=" + sprint.getName() +
                ", Telegram User=" + telegramUser.getName() +
                '}';
    }
}
