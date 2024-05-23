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
    UpdateType updateTypeId;
    
    @ManyToOne
    @JoinColumn(name = "SPRINTID")
    Sprint sprintId;
    
    @ManyToOne
    @JoinColumn(name = "TELEGRAMUSERID")
    TelegramUser telegramUserId;
    
    public SprintUpdate(){

    }
    public SprintUpdate(Long ID, OffsetDateTime timeStamp, UpdateType updateType, Sprint sprint, TelegramUser telegramUser) {
        this.sprintUpdateId = ID;
        this.timeStamp = timeStamp;
        this.updateTypeId = updateType;
        this.sprintId = sprint;
        this.telegramUserId = telegramUser;
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
        return updateTypeId;
    }

    public void setUpdateType(UpdateType updateType) {
        this.updateTypeId = updateType;
    }

    public Sprint getSprint() {
        return sprintId;
    }

    public void setSprint(Sprint sprint) {
        this.sprintId = sprint;
    }

    public TelegramUser getTelegramUser() {
        return telegramUserId;
    }

    public void setTelegramUser(TelegramUser telegramUser) {
        this.telegramUserId = telegramUser;
    }


    @Override
    public String toString() {
        return "Sprint Update{" +
                "ID=" + sprintUpdateId +
                ", TIMESTAMP=" + timeStamp +
                ", Update Type=" + updateTypeId.getName() +
                ", Sprint=" + sprintId.getName() +
                ", Telegram User=" + telegramUserId.getName() +
                '}';
    }
}
