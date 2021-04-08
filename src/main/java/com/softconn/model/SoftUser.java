package com.softconn.model;

import com.softconn.util.SoftUtil;

import java.util.ArrayList;
import java.util.Arrays;

public class SoftUser {
    public static Builder Builder = new Builder();

    public static class Builder {
        private String userId;
        private ArrayList<String> friendList;
        private String email;
        private String password;
        private boolean online;

        public Builder setPassWord(String password) {
            this.password = SoftUtil.hashPw(password);
            return this;
        }

        public Builder setUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder setEmail (String email){
            this.email = email;
            return this;
        }

        public Builder setFriendsList (String... list) {
            this.friendList.addAll(Arrays.asList(list));
            return this;
        }

        public Builder setOnline (int online) {
            this.online = online > 0;
            return this;
        }

        public SoftUser build () {
            return new SoftUser(userId, friendList, email, password, online);
        }


    }

    private final String userId;
    private final ArrayList<String> friendList;
    private final String email;
    private String password;
    private boolean online;




    public boolean getOnline() {
        return online;
    }

    public String getPassword() {
        return password;
    }

    public String getUserId() {
        return userId;
    }

    public ArrayList<String> getFriendList() {
        return friendList;
    }

    public String getEmail() {
        return email;
    }

    public SoftUser(String userId, ArrayList<String> friendList, String email, String password) {
        this.userId = userId;
        this.friendList = friendList;
        this.email = email;
        this.password = password;
    }

    public SoftUser(String userId, ArrayList<String> friendList, String email) {
        this.userId = userId;
        this.friendList = friendList;
        this.email = email;
    }

    public SoftUser(String userId, ArrayList<String> friendList, String email, String password, boolean online) {
        this.userId = userId;
        this.friendList = friendList;
        this.email = email;
        this.password = password;
        this.online = online;
    }

    @Override
    public String toString() {
        return "SoftUser{" +
                "userId='" + userId + '\'' +
                ", friendList=" + friendList +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", online=" + online +
                '}';
    }


    //    public static void main(String[] args) {
//        SoftUser u = SoftUser.Builder.setUserId("a").setEmail("b").setPassWord("c").build();
//        System.out.println(u);
//    }


}
