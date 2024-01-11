package com.fsd08.MediLink.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
//    @Column(name = "sender_id")
//    private int sender_id;
//    @Column(name = "receiver_id")
//    private int receiver_id;
    @Column(name = "body")
    private String body;
    @Column(name = "isread")
    private boolean isread;
    @Column(name = "sent_at")
    private LocalDateTime sent_at;
    @Column(name = "type")
    private String type;
    @Column(name = "isclosed")
    private boolean isclosed;

    @Column(name = "parentmessage_id")
    private int parentmessage_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private User receiver;


    public Message() {
    }

    public Message(int id, User sender, User receiver, String body, boolean isread, LocalDateTime sent_at, String type, boolean isclosed, int parentmessage_id) {
        this.id = id;

        this.sender = sender;
        this.receiver = receiver;
        this.body = body;
        this.isread = isread;
        this.sent_at = sent_at;
        this.type = type;
        this.isclosed = isclosed;
        this.parentmessage_id = parentmessage_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

//    public int getSender_id() {
//        return sender_id;
//    }
//
//    public void setSender_id(int sender_id) {
//        this.sender_id = sender_id;
//    }
//
//    public int getReceiver_id() {
//        return receiver_id;
//    }
//
//    public void setReceiver_id(int receiver_id) {
//        this.receiver_id = receiver_id;
//    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean isIsread() {
        return isread;
    }

    public void setIsread(boolean isread) {
        this.isread = isread;
    }

    public LocalDateTime getSent_at() {
        return sent_at;
    }

    public void setSent_at(LocalDateTime sent_at) {
        this.sent_at = sent_at;
    }

    public String getType() {return type;}

    public void setType(String type) {this.type = type;}

    public boolean isIsclosed() {return isclosed;}

    public void setIsclosed(boolean isclosed) {this.isclosed = isclosed;}

    public int getParentmessage_id() {
        return parentmessage_id;
    }

    public void setParentmessage_id(int parentmessage_id) {
        this.parentmessage_id = parentmessage_id;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }
}
