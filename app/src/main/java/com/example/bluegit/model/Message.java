package com.example.bluegit.model;

import org.threeten.bp.LocalDateTime;

public class Message {
    public String message;
    public LocalDateTime sentTime;

    // TODO: id or object
//    public Account from;
//    public Account to;

    public String fromId;
    public String toId;

    public Message(String message, String fromId, String toId) {
        this.message = message;
        this.sentTime = LocalDateTime.now();
        this.fromId = fromId;
        this.toId = toId;
    }
}
