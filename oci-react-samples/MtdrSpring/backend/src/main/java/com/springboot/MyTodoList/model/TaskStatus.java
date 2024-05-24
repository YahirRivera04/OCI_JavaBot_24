package com.springboot.MyTodoList.model;


import java.util.List;

import javax.persistence.*;
@Entity
@Table(name = "TASKSTATUS")
public class TaskStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
    @Column(name = "TASKSTATUSID")
    Long taskStatusId;
    
    @Column(name = "NAME")
    String name;
    
    @Column(name = "DESCRIPTION")
    String description;
    
    @OneToMany(mappedBy = "taskId", cascade = CascadeType.ALL)
    List<Task> taskId;

    public TaskStatus(){
    }
    
    public TaskStatus(Long ID, String name ,String description) {
        this.taskStatusId = ID;
        this.name = name;
        this.description = description;
    }

    public Long getID() {
        return taskStatusId;
    }

    public void setID(Long ID) {
        this.taskStatusId = ID;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Task> getTasks(){
        return taskId;
    }

    @Override
    public String toString() {
        return "Task Status{" +
                "ID=" + taskStatusId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}