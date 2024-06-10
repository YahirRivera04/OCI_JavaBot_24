package com.springboot.MyTodoList.model;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


@Entity
@Table(name = "CONVERSATION")
public class Conversation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
    @Column(name = "CONVERSATIONID")
    Long conversationId;
    
    @Column(name = "STARTTIME")
    Timestamp startTime;
    
    @Column(name = "ENDTIME")
    Timestamp endTime;
    
    @OneToMany(mappedBy = "conversationIdFk", cascade = CascadeType.ALL)
    List<Message> messageId;
    
    public Conversation(){

    }
    public Conversation(Long ID, Timestamp startTime, Timestamp endTime) {
        this.conversationId = ID;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Long getID() {
        return conversationId;
    }

    public void setID(Long ID) {
        this.conversationId = ID;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }
    
    public List<Message> getMessages(){
        return messageId;
    }

    @Override
    public String toString() {
        return "Conversation{" +
                "ID=" + conversationId +
                ", Start time='" + startTime + '\'' +
                ", End time=" + endTime +
                '}';
    }
}
