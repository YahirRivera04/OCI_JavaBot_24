package com.springboot.MyTodoList.model;


import java.util.List;
import java.util.Iterator;
import javax.persistence.*;
@Entity
@Table(name = "TEAM")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
    @Column(name = "TEAMID")
    Long teamId;
    
    @Column(name = "NAME")
    String name;
    
    @Column(name = "DESCRIPTION")
    String description;
    
    @ManyToOne
    @JoinColumn(name = "TEAMTYPEID")
    TeamType teamTypeIdFk;

    @ManyToMany
    @JoinTable(
        name = "TODOUSER.USERTEAM", 
        joinColumns = @JoinColumn(name = "TEAMID"),  
        inverseJoinColumns = @JoinColumn(name = "TELEGRAMUSERID")
    )
    private List<TelegramUser> telegramUserId;

    public Team(){
    }
    public Team(Long ID, String name ,String description, TeamType teamType) {
        this.teamId = ID;
        this.name = name;
        this.description = description;
        this.teamTypeIdFk = teamType;
    }

    public Long getID() {
        return teamId;
    }

    public void setID(Long ID) {
        this.teamId = ID;
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
        return teamTypeIdFk;
    }

    public void setTeamType(TeamType teamType) {
        this.teamTypeIdFk = teamType;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Team[ID=").append(teamId)
           .append(", Name=").append(name)
           .append(", Description=").append(description)
           .append(", TeamType=").append(teamTypeIdFk != null ? teamTypeIdFk.getName() : "None");

        if (telegramUserId != null && !telegramUserId.isEmpty()) {
            sb.append(", Users=[");
            Iterator<TelegramUser> iterator = telegramUserId.iterator();
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
