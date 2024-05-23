package com.springboot.MyTodoList.model;


import javax.persistence.*;

@Entity
@Table(name = "BOTOPTION")
public class BotOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "BOTOPTIONID")
    Long botOptionId;

    @Column(name = "TEXT")
    String text;

    @Column(name = "DESCRIPTION")
    String description;

    @ManyToOne
    @JoinColumn(name = "BOTMENUID")
    BotMenu botMenuId;
    
    public BotOption(){

    }
    public BotOption(Long ID, String text, String description, BotMenu botMenu) {
        this.botOptionId = ID;
        this.text = text;
        this.description = description;
        this.botMenuId = botMenu;
    }

    public Long getID() {
        return botOptionId;
    }

    public void setID(Long ID) {
        this.botOptionId = ID;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BotMenu getBotMenu() {
        return botMenuId;
    }

    public void setUserType(BotMenu botMenu) {
        this.botMenuId = botMenu;
    }

    @Override
    public String toString() {
        return "Bot Option{" +
                "ID=" + botOptionId +
                ", Text='" + text + '\'' +
                ", Descriprion=" + description +
                ", User Type=" + botMenuId.getName() +
                '}';
    }
}
