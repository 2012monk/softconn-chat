package com.softconn.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.text.SimpleDateFormat;
import java.util.*;

public class SoftRoom {

    public static Builder Builder = new Builder();

    public static class Builder {
        private Map<String, List<SoftUser>> roomUsers;
        private String roomId;
        private String roomName;
        private String crtDate;
        private int currentUsers;
        private List<String> clientList;

        public Builder() {
        }

        public Builder setUserList (List<SoftUser> userList) {
            this.roomUsers = new HashMap<>();
            this.clientList = new ArrayList<>();
            this.roomUsers.put("roomUsers", userList);
            this.currentUsers = userList.size();
            for (SoftUser s: userList){
                clientList.add(s.getUserId());
            }
            return this;
        }


        public Builder setRoomId (String roomId) {
            this.roomId = roomId;
            return this;
        }

        public Builder setRoomName (String roomName) {
            this.roomName = roomName;
            return this;
        }

        public Builder setCrtDate (String crtDate) {
            this.crtDate = crtDate;
            return this;
        }

        public SoftRoom createNewRoom (String roomName) {
            SimpleDateFormat fmt = new SimpleDateFormat("yyMMddHHmmss");
            Date date = new Date();
            this.roomName = roomName;
            this.roomId = fmt.format(date)+roomName+UUID.randomUUID().toString();
            this.crtDate = date.toString();
            return new SoftRoom(roomId, roomName, crtDate);
        }

        public SoftRoom build () {
            return new SoftRoom(roomId, roomName, crtDate, currentUsers,roomUsers, clientList);
        }
    }

    private String roomId;
    private String roomName;
    private String crtDate;
    private int currentUsers;
    @JsonIgnore
    private Map<String, List<SoftUser>> roomUsers;
    @JsonIgnore
    private List<String> clientList;
    public SoftRoom() {}

    public SoftRoom(String roomId, String roomName, String crtDate) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.crtDate = crtDate;
    }

    public SoftRoom(String roomId, String roomName, String crtDate, Map<String, List<SoftUser>> roomUsers) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.crtDate = crtDate;
        this.roomUsers = roomUsers;
    }

    public SoftRoom(String roomId, String roomName, String crtDate, int currentUsers, Map<String, List<SoftUser>> roomUsers) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.crtDate = crtDate;
        this.currentUsers = currentUsers;
        this.roomUsers = roomUsers;
    }

    public SoftRoom(String roomId, String roomName, String crtDate, int currentUsers, Map<String, List<SoftUser>> roomUsers, List<String> clientList) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.crtDate = crtDate;
        this.currentUsers = currentUsers;
        this.roomUsers = roomUsers;
        this.clientList = clientList;
    }

    public String getRoomId() {
        return roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getCrtDate() {
        return crtDate;
    }

    public int getCurrentUsers() {
        return currentUsers;
    }

    @JsonAnyGetter
    public Map<String, List<SoftUser>> getRoomUsers() {
        return roomUsers;
    }



    public void addClient (String userId) {
        clientList.add(userId);
    }
//        roomUsers.get("roomUsers").add(userId);
//        caller.update(roomId, );
//    }

    public void pullClient (String userId) {
//        roomUsers.get("roomUsers").remove(userId);}
        clientList.remove(userId);
    }

    public List<String> getClientList () {
        return clientList;
    }

    public void setRoomUsers(Map<String, List<SoftUser>> roomUsers) {
        this.roomUsers = roomUsers;
        for (SoftUser s:roomUsers.get("roomUsers")){
            this.clientList.add(s.getUserId());
        }
    }

    @Override
    public String toString() {
        return "SoftRoom{" +
                "roomId='" + roomId + '\'' +
                ", roomName='" + roomName + '\'' +
                ", crtDate='" + crtDate + '\'' +
                ", activeUserList=" + roomUsers +
                '}';
    }
}
