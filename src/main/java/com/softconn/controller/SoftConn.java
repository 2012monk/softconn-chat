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
    }


}
