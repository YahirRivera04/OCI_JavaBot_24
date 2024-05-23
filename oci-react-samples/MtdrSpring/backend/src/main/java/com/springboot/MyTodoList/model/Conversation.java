package com.springboot.MyTodoList.model;


import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.List;
@Entity
@Table(name = "CONVERSATION")
public class Conversation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
    @Column(name = "CONVERSATIONID")
    Long conversationId;
    
    @Column(name = "STARTTIME")
    OffsetDateTime startTime;
    
    @Column(name = "ENDTIME")
    OffsetDateTime endTime;
    
    @OneToMany(mappedBy = "MESSAGEID", cascade = CascadeType.ALL)
    List<Message> messageId;
    
    public Conversation(){

    }
    public Conversation(Long ID, OffsetDateTime startTime, OffsetDateTime endTime) {
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

    public OffsetDateTime startTime() {
        return startTime;
    }

    public void startTime(OffsetDateTime endTime) {
        this.endTime = endTime;
    }

    public OffsetDateTime endTime() {
        return endTime;
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
