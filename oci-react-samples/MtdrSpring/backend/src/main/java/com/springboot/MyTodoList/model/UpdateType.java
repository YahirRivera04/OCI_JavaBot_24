package com.springboot.MyTodoList.model;


import java.util.List;

import javax.persistence.*;
@Entity
@Table(name = "UPDATETYPE")
public class UpdateType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UPDATETYPEID")
    Long updateTypeId;
    @Column(name = "NAME")
    String name;
    @Column(name = "DESCRIPTION")
    String description;
    @OneToMany(mappedBy = "taskUpdateId", cascade = CascadeType.ALL)
    List<TaskUpdate> taskUpdates;
    @OneToMany(mappedBy = "sprintUpdateId", cascade = CascadeType.ALL)
    List<SprintUpdate> sprintUpdates;
    

    public UpdateType(){
    }
    public UpdateType(Long ID, String name ,String description) {
        this.updateTypeId = ID;
        this.name = name;
        this.description = description;
    }

    public Long getID() {
        return updateTypeId;
    }

    public void setID(Long ID) {
        this.updateTypeId = ID;
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
                "ID=" + updateTypeId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
