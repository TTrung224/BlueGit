package com.example.bluegit.model;

import com.google.firebase.Timestamp;

import org.threeten.bp.LocalDateTime;


import java.util.Map;
import java.util.Objects;

public class Message {
    private String message;
    private Timestamp sentTime;

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
        this.sentTime = Timestamp.now();
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

    public Timestamp getSentTime() {
        return sentTime;
    }

    public void setSentTime(Timestamp sentTime) {
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
