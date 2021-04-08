package com.softconn.service;

import com.softconn.model.SoftLogInToken;
import com.softconn.model.SoftUser;
import com.softconn.model.UserAcess;
import com.softconn.util.Token;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class UserService {

    private static final HashMap<String, SoftUser> userList = new HashMap<>();
    private static final LinkedList<String> activeUsers = new LinkedList<>();

    private final UserAcess access = new UserAcess();


    public Token auth(String userId, String userPw) {
        SoftLogInToken token =
                SoftLogInToken
                .Builder
                .setUserID(userId)
                .setUserPw(userPw)
                .build();
        return access.login(token);
    }



    public Token create(String userId, String userPw, String email) {
        SoftUser user = SoftUser
                .Builder
                .setUserId(userId)
                .setEmail(email)
                .setPassWord(userPw)
                .build();
        return access.registerUser(user);
    }

    public Token check (String userId) {
        return access.checkOverlap(userId);
    }

    public SoftUser getUserInfo (String userId) {
        return access.getUserInfo(userId);
    }


    public List<SoftUser> getUserList () {
        return UserAcess.users();
    }

    public List<SoftUser> getUserList (String userId) {
        return access.friends(userId);
    }
    
    public Token addFriend(String userId, String friendId) {
        return access.insertNetwork(userId, friendId);
    }










}
