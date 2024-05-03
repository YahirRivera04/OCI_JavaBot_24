package com.springboot.MyTodoList.model;


import java.util.List;
import javax.persistence.*;

@Entity
@Table(name = "USERTYPE")
public class UserType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UserTypeId")
    int ID;
    @Column(name = "Name")
    String name;
    @Column(name = "Description")
    String description;
    @OneToMany(mappedBy = "userType", cascade = CascadeType.ALL)
    List<TelegramUser> telegramUsers; 
    @OneToMany(mappedBy = "userType", cascade = CascadeType.ALL)
    List<BotMenu> botMenus; 
    public UserType(){
    }
    public UserType(int ID, String name ,String description) {
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

    public List<TelegramUser> getTelegramUsers() {
        return telegramUsers;
    }

    public List<BotMenu> getBotMenus() {
        return botMenus;
    }

    @Override
    public String toString() {
        return "UserType{" +
                "ID=" + ID +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
