package com.example.bluegit.model;

import org.threeten.bp.LocalDateTime;

import java.util.Map;
import java.util.Objects;

public class Message {
    private String message;
    private LocalDateTime sentTime;

    // TODO: id or object
//    public Account from;
//    public Account to;

    private String fromId;
    private String toId;

    @Override
    public String toString() {
        return "Message{" +
                "message='" + message + '\'' +
                ", sentTime=" + sentTime +
                ", fromId='" + fromId + '\'' +
                ", toId='" + toId + '\'' +
                '}';
    }

    public Message(){}

    public Message(String message, String fromId, String toId) {
        this.message = message;
        this.sentTime = LocalDateTime.now();
        this.fromId = fromId;
        this.toId = toId;
    }

    public Message(Map<String, Object> message) {
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getSentTime() {
        return sentTime;
    }

    public void setSentTime(LocalDateTime sentTime) {
        this.sentTime = sentTime;
    }

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public String getToId() {
        return toId;
    }

    public void setToId(String toId) {
        this.toId = toId;
    }
}
