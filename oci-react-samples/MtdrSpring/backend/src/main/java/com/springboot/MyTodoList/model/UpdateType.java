package com.springboot.MyTodoList.model;


import java.util.List;

import javax.persistence.*;
@Entity
@Table(name = "UPDATETYPE")
public class UpdateType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UpdateTypeId")
    int ID;
    @Column(name = "Name")
    String name;
    @Column(name = "Description")
    String description;
    @OneToMany(mappedBy = "updateType", cascade = CascadeType.ALL)
    List<TaskUpdate> taskUpdates;
    @OneToMany(mappedBy = "updateType", cascade = CascadeType.ALL)
    List<SprintUpdate> sprintUpdates;
    

    public UpdateType(){
    }
    public UpdateType(int ID, String name ,String description) {
        this.ID = ID;
        this.name = name;
        this.description = description;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
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

    public List<TaskUpdate> getTaskUpdates(){
        return taskUpdates;
    }

    public List<SprintUpdate> getSprintUpdates(){
        return sprintUpdates;
    }

    @Override
    public String toString() {
        return "UpdateType{" +
                "ID=" + ID +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
