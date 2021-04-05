package com.softconn.controller;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.softconn.DB.SessionHandler;
import com.softconn.model.SoftUser;
import com.softconn.service.UserService;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.StringWriter;

//@WebServlet(name = "SoftConn", value = "/conn/*")
public class SoftConn extends HttpServlet {

    private static final UserService service = new UserService();
    private static final SessionHandler handler = new SessionHandler();
    private static final MustacheFactory mf = new DefaultMustacheFactory();
    private static final StringWriter writer = new StringWriter();



    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.getRequestDispatcher("/main.html").forward(request, response);

//        String userId = SessionHandler.getUserId(request);
//        String sessionID = SessionHandler.getSessionId(request);
//        String op = request.getPathInfo();
//        Mustache m = null;
//
//        if (SessionHandler.verifySession(sessionID)) {
////            switch (op.split("/")[1]){
////                case "user-list":
////                    m = mf.compile("user-list.html");
////                    m.execute(writer, service.getUserList());
////            }
//
//            request.getRequestDispatcher("/main.html").forward(request, response);
////            System.out.println(sessionID);
//        }else {
//            request.getRequestDispatcher("/login.html").forward(request, response);
//        }
    }


}
