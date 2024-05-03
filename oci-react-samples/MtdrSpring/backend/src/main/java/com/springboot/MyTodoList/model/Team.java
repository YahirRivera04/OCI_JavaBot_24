package com.springboot.MyTodoList.model;


import java.util.Set;
import java.util.Iterator;
import javax.persistence.*;
@Entity
@Table(name = "TEAM")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TeamId")
    int ID;
    @Column(name = "Name")
    String name;
    @Column(name = "Description")
    String description;
    @ManyToOne
    @JoinColumn(name = "TeamtypeId")
    TeamType teamType;
    @ManyToMany
    @JoinTable(
        name = "USERTEAM", 
        joinColumns = @JoinColumn(name = "TeamId"),  
        inverseJoinColumns = @JoinColumn(name = "TelegramUserId")
    )
    private Set<TelegramUser> telegramUsers;

    public Team(){
    }
    public Team(int ID, String name ,String description, TeamType teamType) {
        this.ID = ID;
        this.name = name;
        this.description = description;
        this.teamType = teamType;
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

    public TeamType getTeamType() {
        return teamType;
    }

    public void setTeamType(TeamType teamType) {
        this.teamType = teamType;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Team[ID=").append(ID)
           .append(", Name=").append(name)
           .append(", Description=").append(description)
           .append(", TeamType=").append(teamType != null ? teamType.getName() : "None");

        if (telegramUsers != null && !telegramUsers.isEmpty()) {
            sb.append(", Users=[");
            Iterator<TelegramUser> iterator = telegramUsers.iterator();
            while (iterator.hasNext()) {
                TelegramUser user = iterator.next();
                sb.append("User[ID=").append(user.getID())
                   .append(", Name=").append(user.getName())
                   .append(", TelegramName=").append(user.getTelegramName()).append("]");
                if (iterator.hasNext()) {
                    sb.append(", ");
                }
            }
            sb.append("]");
        }

        sb.append("]");
        return sb.toString();
    }
}
