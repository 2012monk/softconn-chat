package com.softconn.DB;

import com.softconn.model.UserAcess;
import com.softconn.util.Token;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

import static com.softconn.DB.ConnHandler.close;
import static com.softconn.DB.ConnHandler.getConn;

public class SessionHandler {


    private static final String ENCRYPT_KEY = "";
    Logger log = Logger.getGlobal();
    private static UserAcess access = new UserAcess();

    public static String getSessionId(HttpServletRequest request) {
        HttpSession session = null;
        String sessionId = null;
        try {
            session = request.getSession(false);
            if (session != null){
                sessionId = (String) session.getAttribute("sessionId");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return sessionId;
    }

    public static String getUserId (HttpServletRequest rq) {
        return (String) rq.getSession().getAttribute("userId");
    }

    public static boolean verifySession (String sessionId) {
        return  access.session(sessionId);
    }

    public static boolean verifySession (HttpServletRequest rq) {
        String sessionId = getSessionId(rq);
        if (sessionId != null){
            System.out.println(rq.getSession().getCreationTime());
            return access.session(sessionId);
        }else{
            return false;
        }

    }






    public void createSession (HttpServletRequest rq, String userId) {
//        String sql = "INSERT INTO SESS_REPO (USER_ID, SESSION_ID) VALUES (?,?)";
        String sql = "{call SESS_AUTH(?,?)}";
        String sessId = generateSessionId();
        HttpSession session = rq.getSession();
        session.setAttribute("sessionId", sessId);
        session.setAttribute("userId", userId);
//        log.info((String) session.getAttribute("sessionId"));
//        log.info((String) session.getAttribute("userId"));
        Connection conn = getConn();
        try {
            CallableStatement call = conn.prepareCall(sql);
            call.setString(1, userId);
            call.setString(2, sessId);
//            int t = call.executeUpdate();
//            if (t>0) conn.commit();;
//            System.out.println(t);
            call.execute();
            close(conn);
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String generateSessionId () {
        return UUID.randomUUID().toString();
    }






}
