package com.softconn.util;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class SoftUtil {

    public static String hashPw(String pw){
        StringBuilder cryptPw = new StringBuilder();
        String salt = "cba155f2e5ea1400f9f3cafc8f8093fcd27a99642ab589b3e9f7414b61b901df";
        String encodedPw ="";
        pw = pw+salt;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(pw.getBytes(StandardCharsets.UTF_8));
            System.out.println(cryptPw);
            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) cryptPw.append('0');
                cryptPw.append(hex);
            }
            for (byte b:hash) {
                cryptPw.append(Integer.toString((0xff&b)+0x100, 16));
            }
            encodedPw = Base64.encode(hash);
            return encodedPw;
        }catch (Exception e){
        }
        return encodedPw;
    }


    public static Token verifyUser (HttpServletRequest rq) {
        String key = (String) rq.getSession().getAttribute("sessionId");
        if (key == null) return Token.USER_IN_SESSION;
        else return Token.NEED_VERIFICATION;
    }

    public static boolean verifySession (HttpServletRequest rq) {
        String key = (String) rq.getSession().getAttribute("sessionId");
        return key == null;
    }

}
