package com.softconn.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.softconn.util.MessageType;

import java.io.Serializable;
import java.util.Map;

@JsonIgnoreProperties({"message", "roomCounts", "user"})
public class NotifyMessage implements Serializable {


    private MessageType type;
    private String message;
    private String userId;
    private Map<String, Integer> list;


    public NotifyMessage() {
    }

    public NotifyMessage(MessageType type, String message, String userId, Map<String, Integer> list) {
        this.type = type;
        this.message = message;
        this.userId = userId;
        this.list = list;
    }

    public MessageType getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public String getUserId() {
        return userId;
    }

    public Map<String, Integer> getList() {
        return list;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setList(Map<String, Integer> list) {
        this.list = list;
    }

//    public void setList()
}
