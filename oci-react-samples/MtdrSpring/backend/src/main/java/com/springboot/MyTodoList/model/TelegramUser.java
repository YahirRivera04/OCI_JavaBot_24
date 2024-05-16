package com.springboot.MyTodoList.model;

import java.util.Set;
import javax.persistence.*;
import java.util.Iterator;
import java.util.List;

@Entity
@Table(name = "TELEGRAMUSER")
public class TelegramUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TELEGRAMUSERID")
    Long ID;
    @Column(name = "NAME")
    String name;
    @Column(name = "EMAIL")
    String email;
    @Column(name = "PHONENUMBER")
    String phoneNumber;
    @Column(name = "TELEGRAMNAME")
    String telegramName;
    @ManyToOne
    @JoinColumn(name = "USERTYPEID")
    UserType userType;
    @Column(name = "CHATID")
    Long chatId;
    @ManyToMany
    @JoinTable(
        name = "USERTEAM", 
        joinColumns = @JoinColumn(name = "TelegramUserId"),  
        inverseJoinColumns = @JoinColumn(name = "TeamId")
    )
    private Set<Team> teams; 
    @OneToMany(mappedBy = "telegramUser", cascade = CascadeType.ALL)
    List<Task> tasks;
    @OneToMany(mappedBy = "telegramUser", cascade = CascadeType.ALL)
    List<TaskUpdate> taskUpdates;
    @OneToMany(mappedBy = "telegramUser", cascade = CascadeType.ALL)
    List<SprintUpdate> sprintUpdates;
    @OneToMany(mappedBy = "telegramUser", cascade = CascadeType.ALL)
    List<Message> messages;
    public TelegramUser() {
    }

    
    public TelegramUser(Long ID, String telegramName, UserType userType) {
        this.ID = ID;
        this.name = "No name";
        this.email = "No email";
        this.phoneNumber = "No phone Number";
        this.telegramName = telegramName;
        this.userType = userType;
    }
    public TelegramUser(Long ID, String name ,String email, String phoneNumber, String telegramName, UserType userType) {
        this.ID = ID;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.telegramName = telegramName;
        this.userType = userType;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getTelegramName() {
        return telegramName;
    }

    public void setTelegramName(String telegramName) {
        this.telegramName = telegramName;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public Long getChatIds(){
        return chatId;
    }

    public void setChatIds(Long chatIds){
        this.chatId = chatIds;
    }

    public Set<Team> getTeams() {
        return teams;
    }

    public void setTeams(Set<Team> teams) {
        this.teams = teams;
    }

    public List<Task> getTasks(){
        return tasks;
    }

    public List<TaskUpdate> getTaskUpdates(){
        return taskUpdates;
    }

    public List<SprintUpdate> getSprintUpdates(){
        return sprintUpdates;
    }

    public List<Message> getMessages(){
        return messages;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TelegramUser[ID=").append(ID)
           .append(", Name=").append(name)
           .append(", Email=").append(email)
           .append(", PhoneNumber=").append(phoneNumber)
           .append(", TelegramName=").append(telegramName)
           .append(", UserType=").append(userType != null ? userType.getName() : "None")
           .append(", ChatId=").append(chatId);

        if (!teams.isEmpty()) {
            sb.append(", Teams=[");
            Iterator<Team> iterator = teams.iterator();
            while (iterator.hasNext()) {
                Team team = iterator.next();
                sb.append("Team[Name=").append(team.getName())
                   .append(", Description=").append(team.getDescription()).append("]");
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
