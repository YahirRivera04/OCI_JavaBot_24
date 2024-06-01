package com.springboot.MyTodoList.model;
import javax.persistence.*;

@Entity
@Table(name = "USERTEAM")
public class UserTeam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
    @Column(name = "USERTEAMID")
    Long teamId;
    
    @ManyToOne
    @JoinColumn(name = "TELEGRAMUSERID", referencedColumnName = "TELEGRAMUSERID")
    private TelegramUser telegramUser;
    
    @ManyToOne
    @JoinColumn(name = "TEAMID", referencedColumnName = "TEAMID")
    private Team team;
    

    public UserTeam(){
    }
    public UserTeam(Long ID, TelegramUser telegramUser, Team team) {
        this.teamId = ID;
        this.telegramUser = telegramUser;
        this.team = team;
        
    }

    public Long getID() {
        return teamId;
    }

    public void setID(Long ID) {
        this.teamId = ID;
    }
    

    public TelegramUser getTelegramUser(){
        return telegramUser;
    }

    public void setTelegramUser(TelegramUser telegramUser){
        this.telegramUser = telegramUser;
    }

    public Team getTeam(){
        return team;
    }

    public void setTeams(Team team){
        this.team = team;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Team[ID=").append(teamId)
           .append(", Telegram User Id =").append(telegramUser.getID())
           .append(", Team Id=").append(team.getID());
        return sb.toString();
    }
}
