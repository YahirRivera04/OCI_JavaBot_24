package com.springboot.MyTodoList.model;


import java.sql.Timestamp;

import javax.persistence.*;
@Entity
@Table(name = "SPRINTUPDATE")
public class SprintUpdate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
    @Column(name = "SPRINTUPDATEID")
    Long sprintUpdateId;
    
    @Column(name = "TIMESTAMP")
    Timestamp timeStamp;
    
    @ManyToOne
    @JoinColumn(name = "UPDATETYPEID")
    UpdateType updateTypeIdFk;
    
    @ManyToOne
    @JoinColumn(name = "SPRINTID")
    Sprint sprintIdFk;
    
    @ManyToOne
    @JoinColumn(name = "TELEGRAMUSERID")
    TelegramUser telegramUserIdFk;
    
    public SprintUpdate(){

    }
    public SprintUpdate(Long ID, Timestamp timeStamp, UpdateType updateType, Sprint sprint, TelegramUser telegramUser) {
        this.sprintUpdateId = ID;
        this.timeStamp = timeStamp;
        this.updateTypeIdFk = updateType;
        this.sprintIdFk = sprint;
        this.telegramUserIdFk = telegramUser;
    }

    public Long getID() {
        return sprintUpdateId;
    }

    public void setID(Long ID) {
        this.sprintUpdateId = ID;
    }

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }

    public UpdateType getUpdateType() {
        return updateTypeIdFk;
    }

    public void setUpdateType(UpdateType updateType) {
        this.updateTypeIdFk = updateType;
    }

    public Sprint getSprint() {
        return sprintIdFk;
    }

    public void setSprint(Sprint sprint) {
        this.sprintIdFk = sprint;
    }

    public TelegramUser getTelegramUser() {
        return telegramUserIdFk;
    }

    public void setTelegramUser(TelegramUser telegramUser) {
        this.telegramUserIdFk = telegramUser;
    }


    @Override
    public String toString() {
        return "Sprint Update{" +
                "ID=" + sprintUpdateId +
                ", TIMESTAMP=" + timeStamp +
                ", Update Type=" + updateTypeIdFk.getName() +
                ", Sprint=" + sprintIdFk.getName() +
                ", Telegram User=" + telegramUserIdFk.getName() +
                '}';
    }
}
