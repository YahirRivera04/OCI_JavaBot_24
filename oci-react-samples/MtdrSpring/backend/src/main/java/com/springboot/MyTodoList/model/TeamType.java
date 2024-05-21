package com.springboot.MyTodoList.model;

import java.util.List;
import javax.persistence.*;
@Entity
@Table(name = "TEAMTYPE")
public class TeamType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TeamTypeId")
    int ID;

    @Column(name = "Name")
    String name;
    
    @Column(name = "Description")
    String description;
    
    @OneToMany(mappedBy = "teamType", cascade = CascadeType.ALL)
    List<Team> teams;  
    public TeamType(){
    }
    public TeamType(int ID, String name ,String description) {
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

    public List<Team> getTeamList(){
        return teams;
    }

    @Override
    public String toString() {
        return "TeamType{" +
                "ID=" + ID +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
