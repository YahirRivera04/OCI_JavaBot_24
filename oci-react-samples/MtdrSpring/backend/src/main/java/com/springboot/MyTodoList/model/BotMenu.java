package com.springboot.MyTodoList.model;


import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "BOTMENU")
public class BotMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BotMenuId")
    int ID;
    @Column(name = "Name")
    String name;
    @Column(name = "Description")
    String description;
    @ManyToOne
    @JoinColumn(name = "UserTypeId")
    UserType userType;
    @OneToMany(mappedBy = "BotMenuId", cascade = CascadeType.ALL)
    List<BotOption> botOptions;
    public BotMenu(){

    }
    public BotMenu(int ID, String name, String description, UserType userType) {
        this.ID = ID;
        this.name = name;
        this.description = description;
        this.userType = userType;
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

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public List<BotOption> getBotOptions(){
        return botOptions;
    }

    @Override
    public String toString() {
        return "Bot Menu{" +
                "ID=" + ID +
                ", Name='" + name + '\'' +
                ", Descriprion=" + description +
                ", User Type=" + userType.getName() +
                '}';
    }
}
