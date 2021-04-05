package com.softconn.model;

import com.softconn.util.Token;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//import org.apache.log4j.Logger;
//import org.apache.logging.log4j.core.Logger;

import java.sql.*;


import static com.softconn.DB.ConnHandler.close;
import static com.softconn.DB.ConnHandler.getConn;

public class AuthUnit {

    static Logger log = LogManager.getLogger(AuthUnit.class);

    private Token token;

    public static Token getVerified(SoftLogInToken user) {
        String sql = "{call GETAUTH(?,?,?)}";
        Connection conn = getConn();
        ResultSet rs = null;
        CallableStatement call = null;
        PreparedStatement prst = null;
        Token token = Token.NOT_VERIFIED;
        try {
            call = conn.prepareCall(sql);
            call.setString(1, user.getUserID());
            call.setString(2, user.getUserPw());
            call.setInt(3,0);
            call.registerOutParameter(3, Types.INTEGER);
            log.info(user.getUserID()+user.getUserPw());
            log.debug(call.executeUpdate());

            int t = call.getInt(3);
            if (t == 1) token = Token.VERIFIED;
            log.debug(token);
            close(conn);
            return token;
        }catch (Exception e){
            e.printStackTrace();
        }
        return token;
    }


//    public static void main(String[] args) {
//        SoftLogInToken info = new SoftLogInToken("user", "1234");
//        System.out.println(getVerified(info));
//    }
}
