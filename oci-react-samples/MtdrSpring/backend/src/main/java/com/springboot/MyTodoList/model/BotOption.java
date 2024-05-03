package com.springboot.MyTodoList.model;


import javax.persistence.*;

@Entity
@Table(name = "BOTOPTION")
public class BotOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BotOptionId")
    int ID;
    @Column(name = "Text")
    String text;
    @Column(name = "Description")
    String description;
    @ManyToOne
    @JoinColumn(name = "BotMenuId")
    BotMenu botMenu;
    public BotOption(){

    }
    public BotOption(int ID, String text, String description, BotMenu botMenu) {
        this.ID = ID;
        this.text = text;
        this.description = description;
        this.botMenu = botMenu;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
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
        return botMenu;
    }

    public void setUserType(BotMenu botMenu) {
        this.botMenu = botMenu;
    }

    @Override
    public String toString() {
        return "Bot Option{" +
                "ID=" + ID +
                ", Text='" + text + '\'' +
                ", Descriprion=" + description +
                ", User Type=" + botMenu.getName() +
                '}';
    }
}
