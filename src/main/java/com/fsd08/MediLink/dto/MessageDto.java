package com.fsd08.MediLink.dto;

import com.fsd08.MediLink.entity.Message;
import com.fsd08.MediLink.entity.User;

import java.time.LocalDateTime;

public class MessageDto {
    private int id;
    private int sender_id;
    private String sender_username;
    private String sender_avatar;
    private int receiver_id;
    private String receiver_username;
    private String receiver_avatar;
    private String body;
    private boolean isread;
    private LocalDateTime sent_at;
    private String type;
    private boolean isclosed;
    private int parentmessage_id;

    public MessageDto() {
    }

    public MessageDto(int id, int sender_id, String sender_username, String sender_avatar, int receiver_id, String receiver_username, String receiver_avatar, String body, boolean isread, LocalDateTime sent_at, String type, boolean isclosed, int parentmessage_id) {
        this.id = id;
        this.sender_id = sender_id;
        this.sender_username = sender_username;
        this.sender_avatar = sender_avatar;
        this.receiver_id = receiver_id;
        this.receiver_username = receiver_username;
        this.receiver_avatar = receiver_avatar;
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

    public int getSender_id() {
        return sender_id;
    }

    public void setSender_id(int sender_id) {
        this.sender_id = sender_id;
    }

    public String getSender_username() {
        return sender_username;
    }

    public void setSender_username(String sender_username) {
        this.sender_username = sender_username;
    }

    public String getSender_avatar() {
        return sender_avatar;
    }

    public void setSender_avatar(String sender_avatar) {
        this.sender_avatar = sender_avatar;
    }

    public int getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(int receiver_id) {
        this.receiver_id = receiver_id;
    }

    public String getReceiver_username() {
        return receiver_username;
    }

    public void setReceiver_username(String receiver_username) {
        this.receiver_username = receiver_username;
    }

    public String getReceiver_avatar() {
        return receiver_avatar;
    }

    public void setReceiver_avatar(String receiver_avatar) {
        this.receiver_avatar = receiver_avatar;
    }

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isIsclosed() {
        return isclosed;
    }

    public void setIsclosed(boolean isclosed) {
        this.isclosed = isclosed;
    }

    public int getParentmessage_id() {
        return parentmessage_id;
    }

    public void setParentmessage_id(int parentmessage_id) {
        this.parentmessage_id = parentmessage_id;
    }

    public static MessageDto fromEntity(Message message) {
        MessageDto dto = new MessageDto();
        dto.setId(message.getId());
        dto.setSender_id(message.getSender().getId());
        dto.setSender_username(message.getSender().getUsername());
        dto.setSender_avatar(message.getSender().getAvatar());
        dto.setReceiver_id(message.getReceiver().getId());
        dto.setReceiver_username(message.getReceiver().getUsername());
        dto.setReceiver_avatar(message.getReceiver().getAvatar());
        dto.setBody(message.getBody());
        dto.setIsread(message.isIsread());
        dto.setSent_at(message.getSent_at());
        dto.setType(message.getType());
        dto.setIsclosed(message.isIsclosed());
        dto.setParentmessage_id(message.getParentmessage_id());
        return dto;
    }

    public Message toEntity() {
        Message message = new Message();
        message.setId(this.id);
        message.setBody(this.body);
        message.setIsread(this.isIsread());
        message.setSent_at(this.getSent_at());
        message.setType(this.getType());
        message.setIsclosed(this.isIsclosed());
        message.setParentmessage_id(this.getParentmessage_id());

        User sender = new User();
        sender.setId(this.sender_id);
        message.setSender(sender);

        User receiver = new User();
        receiver.setId(this.receiver_id);
        message.setReceiver(receiver);

        return message;
    }

}
