package com.springboot.MyTodoList.model;

import java.util.List;
import javax.persistence.*;
@Entity
@Table(name = "TEAMTYPE")
public class TeamType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TEAMTYPEID")
    Long teamTypeId;

    @Column(name = "NAME")
    String name;
    
    @Column(name = "DESCRIPTION")
    String description;
    
    @OneToMany(mappedBy = "teamTypeIdFk", cascade = CascadeType.ALL)
    List<Team> teamId;  
    
    public TeamType(){
    }
    
    public TeamType(Long ID, String name ,String description) {
        this.teamTypeId = ID;
        this.name = name;
        this.description = description;
    }

    public Long getID() {
        return teamTypeId;
    }

    public void setID(Long ID) {
        this.teamTypeId = ID;
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
        return teamId;
    }

    @Override
    public String toString() {
        return "TeamType{" +
                "ID=" + teamTypeId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
