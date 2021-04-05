package com.softconn.model;

import com.softconn.util.Token;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.softconn.DB.ConnHandler.getConn;
import static com.softconn.DB.ConnHandler.close;

public class UserAcess {

    private final AuthUnit authUnit = new AuthUnit();

    public Token registerUser(SoftUser user) {
        String sql = "INSERT INTO LOGAUTH VALUES (?,?)";
        Connection conn = getConn();
        PreparedStatement prst = null;
        try {
            prst = conn.prepareStatement(sql);
            prst.setString(1, user.getUserId());
            prst.setString(2, user.getPassword());
            int t = prst.executeUpdate();
            if (t > 0) conn.commit();
            close(conn);
            return t > 0 ? Token.CREATE_SUCCESS : Token.CREATE_DENIED;
        }catch (Exception e){
            e.printStackTrace();
        }
        return Token.CREATE_DENIED;
    }

    public int deleteUser (SoftUser user) {
        return -1;
    }

    public int update (SoftUser user) {
        return -1;
    }

    public void getFriendsList (String userId) {

    }

    public Token checkOverlap (String userId) {
        String sql = "SELECT COUNT(*) c FROM LOGAUTH WHERE USER_ID=(?)";
        Connection conn = getConn();
        PreparedStatement prst = null;
        ResultSet rs = null;

        try{
            prst = conn.prepareStatement(sql);
            prst.setString(1, userId);
            rs = prst.executeQuery();
            if (rs.next()){
                int t = rs.getInt(1);
                if (t == 0) return Token.ID_FREE;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return Token.ID_OVERLAP;
    }

    public Token login(SoftLogInToken token) {
        return AuthUnit.getVerified(token);
    }

    public boolean session (String sessionId) {
        String sql = "SELECT * FROM SESS_REPO WHERE SESSION_ID=(?)";
        Connection conn = getConn();
        PreparedStatement prst = null;
        ResultSet rs = null;
        try {
            prst = conn.prepareStatement(sql);
            prst.setString(1, sessionId);
            rs = prst.executeQuery();
            if (rs.next()) {
//                return rs.getString(1);
                return true;
            }
            close(conn);
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public SoftUser getUserInfo (String userId) {
        String sql = "SELECT * FROM USER_INFO WHERE USER_ID=?";
        Connection conn = getConn();
        try {
            PreparedStatement prst = conn.prepareStatement(sql);
            prst.setString(1, userId);
            ResultSet rs = prst.executeQuery();

            ResultSetMetaData md = prst.getMetaData();
            if (rs.next()) {
                return SoftUser
                        .Builder
                        .setUserId(userId)
                        .setEmail(rs.getString("EMAIL"))
                        .build();
            }
            close(conn);
        }catch (Exception e){

        }
        return null;
    }

    public static List<SoftUser> users() {
        ArrayList<SoftUser> userList = new ArrayList<>();
        String sql= "SELECT USER_INFO.USER_ID , " +
                "DECODE(SESSION_ID, NULL, 0, 1) ISONLINE " +
                "FROM USER_INFO " +
                "LEFT OUTER JOIN SESS_REPO SR on USER_INFO.USER_ID = SR.USER_ID";
        Connection conn = getConn();
        try {
            ResultSet rs = conn.prepareStatement(sql).executeQuery();
            while (rs.next()) {
                userList.add(SoftUser
                .Builder
                .setUserId(rs.getString("USER_ID"))
                        .setOnline(rs.getInt("ISONLINE"))
                        .build());
            }
            return userList;

        }catch (Exception e) {
            e.printStackTrace();
        }
        return userList;
    }

    public static List<SoftUser> users(String roomId) {
        ArrayList<SoftUser> userList = new ArrayList<>();
        String sql= "SELECT USER_INFO.USER_ID , " +
                "DECODE(SESSION_ID, NULL, 0, 1) ISONLINE " +
                "FROM USER_INFO " +
                "LEFT OUTER JOIN SESS_REPO SR on USER_INFO.USER_ID = SR.USER_ID";
        Connection conn = getConn();
        try {
            ResultSet rs = conn.prepareStatement(sql).executeQuery();
            while (rs.next()) {
                userList.add(SoftUser
                .Builder
                .setUserId(rs.getString("USER_ID"))
                        .setOnline(rs.getInt("ISONLINE"))
                        .build());
            }
            return userList;

        }catch (Exception e) {
            e.printStackTrace();
        }
        return userList;
    }



    public List<SoftUser> friends (String userId) {
        ArrayList<SoftUser> friendsList = new ArrayList<>();
        String sql="SELECT NETWORK.FRIEND_ID , DECODE(SESSION_ID, null, 0,1) ISONLINE  FROM NETWORK " +
                "LEFT OUTER JOIN SESS_REPO SR on NETWORK.FRIEND_ID = SR.USER_ID " +
                "WHERE NETWORK.USER_ID=?";

        Connection conn = getConn();
        try {
            PreparedStatement prst = conn.prepareStatement(sql);
            prst.setString(1, userId);
            ResultSet rs = prst.executeQuery();
            while (rs.next()) {

                friendsList.add(SoftUser
                        .Builder
                        .setUserId(rs.getString("FRIEND_ID"))
                        .setOnline(rs.getInt("ISONLINE"))
                        .build());
            }
            close(conn);


            return friendsList;

        }catch (Exception e) {
            e.printStackTrace();
        }
        return friendsList;
    }

    public static void main(String[] args) {
        System.out.println(insertNetwork("BBB", "yammy"));
    }

    public static Token insertNetwork(String userId, String friendId) {
        String sql = "{CALL NET_IN(?,?,?)}";
        Connection conn = getConn();
        try {
            CallableStatement call = conn.prepareCall(sql);
            call.setString(1, userId);
            call.setString(2, friendId);
            call.registerOutParameter(3,Types.INTEGER);
//            call.setInt(3, 0);
//            call.executeQuery();
//            ResultSetMetaData md = call.getMetaData();
            ResultSet rs = call.executeQuery();
            int t = call.getInt(3);
            close(conn);
            return t == 1 ? Token.ADD_SUCCESS : Token.ADD_FAILED;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return Token.ADD_FAILED;

    }


}
