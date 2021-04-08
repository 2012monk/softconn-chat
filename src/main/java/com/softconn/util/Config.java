package com.softconn.util;

import java.io.*;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

public class Config {

//    private static final String PATH = "./src/main/resources/config.properties";
    public static String HOME_URL;
    public static String DB_URL;
    public static String DB_ID;
    public static String DB_PW;
    public static String OUT_URL;
    public static String DRIVER;
//    public static String


    static {
        Properties p = new Properties();
//        File path = new File("/config.properties");
        try {
//            System.out.println("/n25");
            System.out.println(Config.class.getClassLoader().getResource("/config.properties"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        URL path = Config.class.getClassLoader().getResource("/config.properties");


//        InputStreamReader isr = new InputStreamReader(path);
//        BufferedReader br = new BufferedReader(isr);
//        URI path = new URI("/config.properties");
//        System.out.println(path.getAbsolutePath());
        try {
//            System.out.println(path);
            File file = null;
            URI uri = null;
            if (path == null){
//                System.out.println(uri);
                file = new File("./src/main/resources/config.properties");
            }else{
                uri = path.toURI();
                file = new File(uri);
            }

            FileReader fr = new FileReader(file);

            p.load(fr);
//            System.out.println(p.getProperty("HOME_URL"));
            Field[] fields = Config.class.getDeclaredFields();

            for (Field f: fields){
                f.setAccessible(false);
                if (f.getModifiers() != 25){
                    f.set(String.class, p.getProperty(f.getName()));

                }

            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
//
//    public static void main(String[] args) {
//        System.out.println(Config.DB_ID);
//    }

}
