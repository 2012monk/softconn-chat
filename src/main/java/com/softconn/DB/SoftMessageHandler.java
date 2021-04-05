package com.softconn.DB;

import com.softconn.model.SoftMessage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.UUID;

import static com.softconn.DB.ConnHandler.close;
import static com.softconn.DB.ConnHandler.getConn;

public class SoftMessageHandler {

    public void absenceMsgHandle (SoftMessage msg) {
        handleMsgLog(msg, false);
    }

    public void handleMsgLog (SoftMessage msg, boolean isRead) {
        String sql = "INSERT INTO MSG_REPO (" +
                "MSG_NUM, MSG_BODY, MSG_TYPE, SENDER_ID, FRIEND_ID, ROOM_ID, IS_READ)" +
                "VALUES (?,?,?,?,?,?,?)";
        Connection conn = getConn();
        String msgId = UUID.randomUUID().toString();

        try {
            PreparedStatement prst = conn.prepareStatement(sql);
            prst.setString(1, msgId);
            prst.setString(2, msg.getMessage());
            prst.setString(3, msg.getType().name());
            prst.setString(4, msg.getSenderId());
            prst.setString(5, msg.getReceiverId());
            prst.setString(6, msg.getRoomId());
            int r = 0;
            if (isRead) r=1;
            prst.setInt(7, r);
            if (prst.executeUpdate() > 0) {
                conn.commit();
            }
            close(conn);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleMsgLog (SoftMessage msg) {
        handleMsgLog(msg, true);
    }
}
