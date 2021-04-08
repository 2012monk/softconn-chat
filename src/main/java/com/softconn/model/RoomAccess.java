package com.softconn.model;

import com.softconn.util.Token;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.softconn.DB.ConnHandler.close;
import static com.softconn.DB.ConnHandler.getConn;

public class RoomAccess {



    public List<SoftRoom> getAllRoom () {
//        String sql = "SELECT * FROM ROOM_REPO";
        String sql = "SELECT * FROM soft.ROOM_REPO";
        Connection conn = getConn();
        ArrayList<SoftRoom> list = new ArrayList<>();
        Field[] args = SoftRoom.class.getDeclaredFields();
        try {
            PreparedStatement prst = conn.prepareStatement(sql);
            ResultSet rs = prst.executeQuery();

            while (rs.next()) {
                List<SoftUser> userList = getRoomUserList(rs.getString("ROOM_ID"));
                list.add(SoftRoom
                        .Builder
                        .setRoomId(rs.getString("ROOM_ID"))
                        .setRoomName(rs.getString("ROOM_NAME"))
                        .setCrtDate(rs.getString("CRT_DATE"))
                        .setUserList(userList)
                        .build());
            }

            return list;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            close(conn);
        }
        return list;
    }






    public SoftRoom getRoomById (String roomId) {
//        String sql = "SELECT * FROM ROOM_REPO WHERE ROOM_ID=?";
        String sql = "SELECT * FROM soft.ROOM_REPO WHERE ROOM_ID=?";
        Connection conn = getConn();
        List<SoftUser> userList = getRoomUserList(roomId);
        try {
            PreparedStatement prst = conn.prepareStatement(sql);
            prst.setString(1, roomId);
            ResultSet rs = prst.executeQuery();

            if (rs.next()) {
                return SoftRoom
                        .Builder
                        .setRoomId(rs.getString("ROOM_ID"))
                        .setRoomName(rs.getString("ROOM_NAME"))
                        .setCrtDate(rs.getString("CRT_DATE"))
                        .setUserList(userList)
                        .build();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            close(conn);
        }
        return null;
    }

//    public List<String> getRoomUserList (String roomId) {
//        ArrayList<String> list = new ArrayList<>();
//        String sql = "SELECT * FROM ROOM_USER_REPO WHERE ROOM_ID=?";
//        Connection conn = getConn();
//        try {
//            PreparedStatement prst = conn.prepareStatement(sql);
//            prst.setString(1, roomId);
//            ResultSet rs = prst.executeQuery();
//            while (rs.next()) {
//                list.add(rs.getString("USER_ID"));
//            }
//            close(conn);
//            return list;
//        }catch (SQLException e){
//            e.printStackTrace();
//        }
//        return list;
//    }

    public List<SoftUser> getRoomUserList (String roomId) {
        ArrayList<SoftUser> list = new ArrayList<>();
//        String sql = "SELECT * FROM ROOM_USER_REPO " +
//                "JOIN USER_INFO UI on ROOM_USER_REPO.USER_ID = UI.USER_ID WHERE ROOM_ID=?";

//        String sql = "SELECT ROOM_ID, UI.USER_ID, DECODE(SESSION_ID, null, 0, 1) ISONLINE FROM ROOM_USER_REPO\n" +
//                "    JOIN USER_INFO UI on ROOM_USER_REPO.USER_ID = UI.USER_ID\n" +
//                "LEFT OUTER JOIN SESS_REPO SR on ROOM_USER_REPO.USER_ID = SR.USER_ID\n" +
//                "WHERE ROOM_ID=?";

        String sql = "SELECT ROOM_ID, ROOM_USER_REPO.USER_ID, ISONLINE FROM soft.ROOM_USER_REPO " +
                "JOIN soft.ONLINE_USERS O ON ROOM_USER_REPO.USER_ID = O.USER_ID WHERE ROOM_ID=?";
        Connection conn = getConn();
        try {
            PreparedStatement prst = conn.prepareStatement(sql);
            prst.setString(1, roomId);
            ResultSet rs = prst.executeQuery();


            while (rs.next()) {
                list.add(SoftUser
                        .Builder
                        .setUserId(rs.getString("USER_ID"))
                        .setOnline(rs.getInt("ISONLINE"))
                        .build());
            }
            close(conn);
            return list;
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            close(conn);
        }
        return list;
    }

    public Token updateRoom (String roomId, String roomName) {
//        String sql = "UPDATE ROOM_REPO SET ROOM_NAME=? WHERE ROOM_ID=?";
        String sql = "UPDATE soft.ROOM_REPO SET ROOM_NAME=? WHERE ROOM_ID=?";
        Connection conn = getConn();
        try {
            PreparedStatement prst = conn.prepareStatement(sql);
            prst.setString(1, roomName);
            prst.setString(2, roomId);
            if (prst.executeUpdate() > 0){
                conn.commit();
                return Token.SUCCESS;
            }
            return Token.FAILED;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            close(conn);
        }
        return Token.ERROR;
    }

    public Token deleteRoom (String roomId, String masterId) {
//        String sql = "DELETE FROM ROOM_REPO WHERE ROOM_REPO.ROOM_ID=?";

        String sql = "DELETE FROM soft.ROOM_REPO WHERE ROOM_REPO.ROOM_ID=? AND MASTER_ID=?";
        Connection conn = getConn();
        try {
            PreparedStatement prst = conn.prepareStatement(sql);
            prst.setString(1, roomId);
            prst.setString(2, masterId);

            
            if (prst.executeUpdate() > 0) {
                conn.commit();
                return Token.SUCCESS;
            }else {
                return Token.FAILED;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            close(conn);
        }
        return Token.ERROR;
    }

    public Token insertRoom (SoftRoom room) {
//        String sql = "INSERT INTO ROOM_REPO (ROOM_ID, ROOM_NAME) " +
//                "VALUES (?,?)";
        String sql = "INSERT INTO soft.ROOM_REPO (ROOM_ID, ROOM_NAME, MASTER_ID) " +
                "VALUES (?,?,?)";
        Connection conn = getConn();
        try {
            PreparedStatement prst = conn.prepareStatement(sql);
            prst.setString(1, room.getRoomId());
            prst.setString(2, room.getRoomName());
            prst.setString(3, room.getMasterId());
            if (prst.executeUpdate() > 0 ){
                conn.commit();
                return Token.ADD_SUCCESS;
            }else {
                return Token.ADD_FAILED;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            close(conn);
        }
        return Token.ERROR;

    }

    public Token insertRoomUser (String roomId, String userId) {
        String sql = "INSERT INTO soft.ROOM_USER_REPO (ROOM_ID, USER_ID) VALUES (?,?) " +
                "ON DUPLICATE KEY UPDATE  ROOM_ID=?";
        Connection conn = getConn();
        try {
            PreparedStatement prst = conn.prepareStatement(sql);
            prst.setString(1, roomId);
            prst.setString(2, userId);
            prst.setString(3, roomId);
            if (prst.executeUpdate() == 1){
                conn.commit();
                return Token.SUCCESS;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            close(conn);
        }


        return Token.FAILED;
    }

    public Token deleteRoomUser (String roomId, String userId) {
        String sql = "DELETE FROM soft.ROOM_REPO WHERE ROOM_ID=? AND MASTER_ID=?";
        Connection conn = getConn();
        try {
            PreparedStatement prst = conn.prepareStatement(sql);
            prst.setString(1, roomId);
            prst.setString(2, userId);

            if (prst.executeUpdate() == 1) {
                conn.commit();
                return Token.SUCCESS;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            close(conn);
        }
        return Token.FAILED;
    }

}
