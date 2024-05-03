package com.springboot.MyTodoList.model;


import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.List;
@Entity
@Table(name = "CONVERSATION")
public class Conversation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ConversationId")
    int ID;
    @Column(name = "StartTime")
    OffsetDateTime startTime;
    @Column(name = "EndTime")
    OffsetDateTime endTime;
    @OneToMany(mappedBy = "ConversationId", cascade = CascadeType.ALL)
    List<Message> messages;
    public Conversation(){

    }
    public Conversation(int ID, OffsetDateTime startTime, OffsetDateTime endTime) {
        this.ID = ID;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
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
        return messages;
    }

    @Override
    public String toString() {
        return "Conversation{" +
                "ID=" + ID +
                ", Start time='" + startTime + '\'' +
                ", End time=" + endTime +
                '}';
    }
}
