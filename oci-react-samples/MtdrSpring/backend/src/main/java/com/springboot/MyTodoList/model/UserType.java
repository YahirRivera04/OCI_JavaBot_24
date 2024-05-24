package com.springboot.MyTodoList.model;


import java.util.List;
import javax.persistence.*;

@Entity
@Table(name = "USERTYPE")
public class UserType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
    @Column(name = "USERTYPEID")
    Long userTypeId;
    
    @Column(name = "NAME")
    String name;
    
    @Column(name = "DESCRIPTION")
    String description;
    
    @OneToMany(mappedBy = "telegramUserId", cascade = CascadeType.ALL)
    List<TelegramUser> telegramUserId; 
    
    @OneToMany(mappedBy = "userTypeId", cascade = CascadeType.ALL)
    List<BotMenu> botMenuId; 
    
    public UserType(){
    }
    
    public UserType(Long ID, String name ,String description) {
        this.userTypeId = ID;
        this.name = name;
        this.description = description;
    }

    public Long getID() {
        return userTypeId;
    }

    public void setID(Long ID) {
        this.userTypeId = ID;
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

    public List<TelegramUser> getTelegramUsers() {
        return telegramUserId;
    }

    public List<BotMenu> getBotMenus() {
        return botMenuId;
    }

    @Override
    public String toString() {
        return "UserType{" +
                "ID=" + userTypeId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
