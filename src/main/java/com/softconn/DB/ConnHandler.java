package com.softconn.DB;

import java.sql.*;

public class ConnHandler {
    private static final String URL;
    private static final String USR;
    private static final String PW;


    static {
        URL = "jdbc:oracle:thin:@localhost:1521:XE";
        USR = "softmaster";
        PW = "softconn";
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
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
