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

    @OneToMany(mappedBy = "team")
    private List<UserTeam> userTeam;

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

    public List<UserTeam> getUserTeams(){
        return userTeam;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Team[ID=").append(teamId)
           .append(", Name=").append(name)
           .append(", Description=").append(description)
           .append(", TeamType=").append(teamTypeIdFk != null ? teamTypeIdFk.getName() : "None");

        if (userTeam != null && !userTeam.isEmpty()) {
            sb.append(", Users=[");
            Iterator<UserTeam> iterator = userTeam.iterator();
            while (iterator.hasNext()) {
                UserTeam user = iterator.next();
                sb.append("UserTeam[ID=").append(user.getID())
                   .append(", Telegram User Id =").append(user.getTelegramUser().getID())
                   .append(", Team Id =").append(user.getTeam().getID()).append("]");
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
