package com.springboot.MyTodoList.model;


import java.util.List;

import javax.persistence.*;
@Entity
@Table(name = "PROJECT")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
    @Column(name = "PROJECTID")
    Long projectId;
    
    @Column(name = "NAME")
    String name;
    
    @Column(name = "DESCRIPTION")
    String description;
    
    @OneToMany(mappedBy = "sprintId", cascade = CascadeType.ALL)
    List<Sprint> sprintId;  

    public Project(){
    }
    public Project(Long ID, String name ,String description) {
        this.projectId = ID;
        this.name = name;
        this.description = description;
    }

    public Long getID() {
        return projectId;
    }

    public void setID(Long ID) {
        this.projectId = ID;
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

    public List<Sprint> getSprints(){
        return sprintId;
    }

    @Override
    public String toString() {
        return "Project{" +
                "ID=" + projectId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
