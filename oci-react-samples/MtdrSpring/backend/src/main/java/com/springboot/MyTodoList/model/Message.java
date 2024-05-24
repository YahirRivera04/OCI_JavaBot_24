package com.springboot.MyTodoList.model;

import javax.persistence.*;
@Entity
@Table(name = "MESSAGE")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
    @Column(name = "MESSAGEID")
    Long messageId;
    
    @Column(name = "CONTENT")
    String content;
    
    @ManyToOne
    @JoinColumn(name = "TELEGRAMUSERID")
    TelegramUser telegramUserIdFk;
    
    @ManyToOne
    @JoinColumn(name = "CONVERSATIONID")
    Conversation conversationIdFk;

    public Message(){
    }
    public Message(Long ID, String content ,TelegramUser telegramUser, Conversation conversation) {
        this.messageId = ID;
        this.content = content;
        this.telegramUserIdFk = telegramUser;
        this.conversationIdFk = conversation;
    }

    public Long getID() {
        return messageId;
    }

    public void setID(Long ID) {
        this.messageId = ID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public TelegramUser getTelegramUser() {
        return telegramUserIdFk;
    }

    public void setTelegramUser(TelegramUser telegramUser) {
        this.telegramUserIdFk = telegramUser;
    }

    public Conversation getConversation() {
        return conversationIdFk;
    }

    public void setConversation(Conversation conversation) {
        this.conversationIdFk = conversation;
    }

    @Override
    public String toString() {
        return "Message{" +
                "ID=" + messageId +
                ", Content='" + content + '\'' +
                ", Telegram User='" + telegramUserIdFk.getTelegramName() + '\'' +
                ", Conversation Id='" + conversationIdFk.getID() + '\'' +
                '}';
    }
}
