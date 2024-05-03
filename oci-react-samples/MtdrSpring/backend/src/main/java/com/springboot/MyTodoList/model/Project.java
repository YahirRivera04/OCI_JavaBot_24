package com.springboot.MyTodoList.model;


import java.util.List;

import javax.persistence.*;
@Entity
@Table(name = "PROJECT")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ProjectId")
    int ID;
    @Column(name = "Name")
    String name;
    @Column(name = "Description")
    String description;
    @OneToMany(mappedBy = "ProjectId", cascade = CascadeType.ALL)
    List<Sprint> sprints;  

    public Project(){
    }
    public Project(int ID, String name ,String description) {
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

    public List<Sprint> getSprints(){
        return sprints;
    }

    @Override
    public String toString() {
        return "Project{" +
                "ID=" + ID +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
