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
    TelegramUser telegramUserId;
    
    @ManyToOne
    @JoinColumn(name = "CONVERSATIONID")
    Conversation conversationId;

    public Message(){
    }
    public Message(Long ID, String content ,TelegramUser telegramUser, Conversation conversation) {
        this.messageId = ID;
        this.content = content;
        this.telegramUserId = telegramUser;
        this.conversationId = conversation;
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
        return telegramUserId;
    }

    public void setTelegramUser(TelegramUser telegramUser) {
        this.telegramUserId = telegramUser;
    }

    public Conversation getConversation() {
        return conversationId;
    }

    public void setConversation(Conversation conversation) {
        this.conversationId = conversation;
    }

    @Override
    public String toString() {
        return "Message{" +
                "ID=" + messageId +
                ", Content='" + content + '\'' +
                ", Telegram User='" + telegramUserId.getTelegramName() + '\'' +
                ", Conversation Id='" + conversationId.getID() + '\'' +
                '}';
    }
}
