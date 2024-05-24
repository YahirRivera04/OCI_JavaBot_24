package com.springboot.MyTodoList.model;


import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "BOTMENU")
public class BotMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
    @Column(name = "BOTMENUID")
    Long botMenuId;
    
    @Column(name = "NAME")
    String name;
    
    @Column(name = "DESCRIPTION")
    String description;
    
    @ManyToOne
    @JoinColumn(name = "USERTYPEID")
    UserType userTypeIdFk;
    
    @OneToMany(mappedBy = "botMenuIdFk", cascade = CascadeType.ALL)
    List<BotOption> botOptionId;
    
    public BotMenu(){

    }
    public BotMenu(Long botMenuId, String name, String description, UserType userType) {
        this.botMenuId = botMenuId;
        this.name = name;
        this.description = description;
        this.userTypeIdFk = userType;
    }

    public long getID() {
        return botMenuId;
    }

    public void setID(Long botMenuId) {
        this.botMenuId = botMenuId;
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
        return userTypeIdFk;
    }

    public void setUserType(UserType userType) {
        this.userTypeIdFk = userType;
    }

    public List<BotOption> getBotOptions(){
        return botOptionId;
    }

    @Override
    public String toString() {
        return "Bot Menu{" +
                "ID=" + botMenuId +
                ", Name='" + name + '\'' +
                ", Descriprion=" + description +
                ", User Type=" + userTypeIdFk.getName() +
                '}';
    }
}
