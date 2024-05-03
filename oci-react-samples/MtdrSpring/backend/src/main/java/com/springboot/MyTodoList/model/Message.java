package com.springboot.MyTodoList.model;

import javax.persistence.*;
@Entity
@Table(name = "MESSAGE")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MessageId")
    int ID;
    @Column(name = "Content")
    String content;
    @ManyToOne
    @JoinColumn(name = "TelegramUserId")
    TelegramUser telegramUser;
    @ManyToOne
    @JoinColumn(name = "ConversationId")
    Conversation conversation;

    public Message(){
    }
    public Message(int ID, String content ,TelegramUser telegramUser, Conversation conversation) {
        this.ID = ID;
        this.content = content;
        this.telegramUser = telegramUser;
        this.conversation = conversation;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public TelegramUser getTelegramUser() {
        return telegramUser;
    }

    public void setTelegramUser(TelegramUser telegramUser) {
        this.telegramUser = telegramUser;
    }

    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    @Override
    public String toString() {
        return "Message{" +
                "ID=" + ID +
                ", Content='" + content + '\'' +
                ", Telegram User='" + telegramUser.getTelegramName() + '\'' +
                ", Conversation Id='" + conversation.getID() + '\'' +
                '}';
    }
}
