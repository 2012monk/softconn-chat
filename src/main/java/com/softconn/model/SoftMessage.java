package com.softconn.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.softconn.util.MessageType;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


@JsonPropertyOrder({"senderId", "receiverId", "message", "roomId", "type", "sendDate", "userList"})
//@JsonIgnoreProperties({"senderId", "roomId", "type", "message", "receiverId", "sendDate"})
public class SoftMessage implements Serializable {

    private static final SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private String message;
    private String roomId;
    private String receiverId;
    private String senderId;
    private String sendDate;
    private MessageType type;
    private HashMap<String, List<String>> userList;
    private int currentUserNumber;

    public SoftMessage(String message, String roomId, String receiverId, String senderId, String sendDate, MessageType type) {
        this.message = message;
        this.roomId = roomId;
        this.receiverId = receiverId;
        this.senderId = senderId;
        this.sendDate = fmt.format(new Date());
        this.type = type;
        this.userList = new HashMap<>();
    }

    public SoftMessage(String message, String roomId, String receiverId,
                       String senderId, String sendDate, MessageType type,
                       HashMap<String, List<String>> userList, int currentUserNumber) {
        this.message = message;
        this.roomId = roomId;
        this.receiverId = receiverId;
        this.senderId = senderId;
        this.sendDate = sendDate;
        this.type = type;
        this.userList = userList;
        this.currentUserNumber = currentUserNumber;
        this.userList = new HashMap<>();
    }

    public SoftMessage(){
        this.userList = new HashMap<>();
        this.sendDate = fmt.format(new Date());
    }

    public void setUserList(List<String> userList) {
        this.currentUserNumber = userList.size();
        this.userList.put("currentUsers", userList);
    }

    public void setCurrentUserNumber(int currentUserNumber) {
        this.currentUserNumber = currentUserNumber;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public void setSendDate(String sendDate) {
        this.sendDate = sendDate;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public MessageType getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public String getRoomId() {
        return roomId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public String getSendDate() {
        return sendDate;
    }

    public String getSenderId() {
        return senderId;
    }

    public HashMap<String, List<String>> getUserList() {
        return userList;
    }

    public int getCurrentUserNumber() {
        return currentUserNumber;
    }

    @Override
    public String toString() {
        return "SoftMessage{" +
                "message='" + message + '\'' +
                ", roomId='" + roomId + '\'' +
                ", receiverId='" + receiverId + '\'' +
                ", sendDate='" + sendDate + '\'' +
                ", type=" + type +
                '}';
    }
}
