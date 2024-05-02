package com.springboot.MyTodoList.model;


import javax.persistence.*;
import java.time.OffsetDateTime;

/*
    representation of the TODOITEM table that exists already
    in the autonomous database
 */
// CREATE TABLE TODOUSER.todoitem (id NUMBER GENERATED ALWAYS AS IDENTITY, description VARCHAR2(4000), creation_ts TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP, done NUMBER(1,0) , PRIMARY KEY (id));
//CREATE TABLE TODOUSER.TASKSTATUS ( TaskStatusId NUMBER PRIMARY KEY, Name NVARCHAR2(100), Description NVARCHAR2(200)); */

@Entity
@Table(name = "TODOITEM")
public class ToDoItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int ID;
    @Column(name = "DESCRIPTION")
    String description;
    @Column(name = "CREATION_TS")
    OffsetDateTime creation_ts;
    @Column(name = "done")
    boolean done;
    public ToDoItem(){

    }
    public ToDoItem(int ID, String description, OffsetDateTime creation_ts, boolean done) {
        this.ID = ID;
        this.description = description;
        this.creation_ts = creation_ts;
        this.done = done;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public OffsetDateTime getCreation_ts() {
        return creation_ts;
    }

    public void setCreation_ts(OffsetDateTime creation_ts) {
        this.creation_ts = creation_ts;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }  


    @Override
    public String toString() {
        return "ToDoItem{" +
                "ID=" + ID +
                ", description='" + description + '\'' +
                ", creation_ts=" + creation_ts +
                ", done=" + done +
                '}';
    }
}
