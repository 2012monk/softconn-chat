package com.softconn.model;

import com.fasterxml.jackson.annotation.*;

import java.io.Serializable;
import java.util.*;

@JsonIgnoreProperties({"users", "rooms", "friends"})
public class SoftList implements Serializable {


    private Map<String, List> list;

    private List<SoftRoom> rooms;
    private List<SoftUser> users;
    private List<SoftUser> friends;
    private String listName;

    public void setListName(String listName) {
        this.listName = listName;
    }

    public void setRooms(List<SoftRoom> rooms) {
        this.rooms = rooms;
        this.list.put("rooms", rooms);
    }

    public void setUsers(List<SoftUser> users) {
        this.users = users;
        this.list.put("user", users);
    }

    public void setFriends(List<SoftUser> friends) {
        this.friends = friends;
    }

    @JsonAnyGetter
    public Map<String, List> getList() {
        return list;
    }

    public List<SoftRoom> getRooms() {
        return rooms;
    }

    public List<SoftUser> getUsers() {
        return users;
    }

    public List<SoftUser> getFriends() {
        return friends;
    }

    public String getListName() {
        return listName;
    }
}
