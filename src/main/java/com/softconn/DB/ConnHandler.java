package com.softconn.DB;

import com.softconn.util.Config;

import java.sql.*;

public class ConnHandler {
    private static final String URL = Config.DB_URL;
    private static final String USR = Config.DB_ID;
    private static final String PW = Config.DB_PW;
    private static final String DRIVER = Config.DRIVER;
//    private static final String URL;
//    private static final String USR;
//    private static final String PW;
//    private static final String DRIVER;

    static {
//        URL = "jdbc:oracle:thin:@112.187.182.64:1521:XE";
//        URL = "jdbc:mariadb://112.187.182.64:3306/soft";
//        USR = "softmaster";
//        PW = "softconn";
//        DRIVER = "org.mariadb.jdbc.Driver";


        System.out.println(URL+USR+PW);
        try {
            Class.forName(DRIVER);
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }



    public static Connection getConn() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USR, PW);
            conn.setAutoCommit(false);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return conn;
    }

    public static void close(Connection conn){
        try {
            if (conn != null) conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void close (PreparedStatement prst) {
        try {
            if (prst != null) prst.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void close (ResultSet rs) {
        try {
            if (rs != null) rs.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
