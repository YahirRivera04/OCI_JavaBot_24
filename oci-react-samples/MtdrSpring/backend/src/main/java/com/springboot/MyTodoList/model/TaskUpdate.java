package com.springboot.MyTodoList.model;
import java.sql.Timestamp;
import javax.persistence.*;

@Entity
@Table(name = "TASKUPDATE")
public class TaskUpdate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
    @Column(name = "TASKUPDATEID")
    Long taskUpdateId;
    
    @Column(name = "TIMESTAMP")
    Timestamp timeStamp;
    
    @ManyToOne
    @JoinColumn(name = "UPDATETYPEID")
    UpdateType updateTypeIdFk;
    
    @ManyToOne
    @JoinColumn(name = "TASKID")
    Task taskIdFk;
    
    @ManyToOne
    @JoinColumn(name = "TELEGRAMUSERID")
    TelegramUser telegramUserIdFk;

    public TaskUpdate(){

    }
    public TaskUpdate(Long ID, Timestamp timeStamp, UpdateType updateType, Task task, TelegramUser telegramUser) {
        this.taskUpdateId = ID;
        this.timeStamp = timeStamp;
        this.updateTypeIdFk = updateType;
        this.taskIdFk = task;
        this.telegramUserIdFk = telegramUser;
    }

    public Long getID() {
        return taskUpdateId;
    }

    public void setID(Long ID) {
        this.taskUpdateId = ID;
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

    public Task getTask() {
        return taskIdFk;
    }

    public void setTask(Task task) {
        this.taskIdFk = task;
    }

    public TelegramUser getTelegramUser() {
        return telegramUserIdFk;
    }

    public void setTelegramUser(TelegramUser telegramUser) {
        this.telegramUserIdFk = telegramUser;
    }


    @Override
    public String toString() {
        return "ToDoItem{" +
                "ID=" + taskUpdateId +
                ", TIMESTAMP=" + timeStamp +
                ", Update Type=" + updateTypeIdFk.getName() +
                ", Task=" + taskIdFk.getName() +
                ", Telegram User=" + telegramUserIdFk.getName() +
                '}';
    }
}
