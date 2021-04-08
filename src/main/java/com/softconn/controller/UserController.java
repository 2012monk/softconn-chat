package com.softconn.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.MustacheFactory;
import com.softconn.DB.SessionHandler;
import com.softconn.model.SoftUser;
import com.softconn.service.UserService;
import com.softconn.util.Token;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@WebServlet("/user/*")
public class UserController extends HttpServlet {
    Logger log = LogManager.getLogger(UserController.class);

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final UserService service = new UserService();
    private static final SessionHandler handler = new SessionHandler();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        response.getWriter().println("sadfadsf");
        String op = request.getPathInfo();
        String[] path = op.split("/");
//        System.out.println(Arrays.toString(path));
        log.info(request.getSession().getAttribute("userId"));
        log.info("FROM GET REQ");
        log.warn(op);

        switch (path[1]){
            case "logout":
                handleLogOut(request, response);
                break;
            case "info":
                sendUserInfo(request, response);
                break;
            case "list":
//                response.setContentType("application/json");
                sendUserList(request, response);
                break;
            case "friends":
//                response.setContentType("application/json");
                sendFriendList(request,response);
                break;
            case "self":
                log.info(request.getSession().getAttribute("userId"));
                response.getWriter().write((String)request.getSession().getAttribute("userId"));
                break;
        }

    }

    @Override
    protected void doPost (HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String op = request.getPathInfo();

        log.warn(op);
        String[] path = op.split("/");
        System.out.println(request.getHeaderNames().nextElement());


//        while (request.getHeaderNames().hasMoreElements()){
//            String hname = request.getHeaderNames().nextElement();
//            log.info(hname);
//            log.info(request.getHeader(hname));
//        }

        switch (path[1]){
            case "signin":
                handleSignIn(request, response);
                break;
            case "login":
                handleLogin(request, response);
                break;
            case "check":
                handleId(request, response);
                break;
            case "info":
                sendUserInfo(request, response);
                break;
            case "add":
                handleAddFriend(request, response);
                break;
            case "friend-info":
                sendFriendInfo(request, response);
                break;
        }


    }


    public void handleLogin (HttpServletRequest request, HttpServletResponse res) throws IOException, ServletException {
        String userId = request.getParameter("user-id");
        String pw = request.getParameter("user-pw");
        log.warn(userId+pw);
        Token token = service.auth(userId, pw);
        if (token == Token.VERIFIED){
            handler.createSession(request, userId);
            log.info(request.getParameter("sessionId"));
            res.getWriter().write("LOGINSUCESS");
        }else {
            res.getWriter().write("FAILED");
        }
//        request.getRequestDispatcher("/").forward(request, res);
    }

    public void handleLogOut (HttpServletRequest rq, HttpServletResponse rs) throws ServletException, IOException {
        rq.getSession().invalidate();
        rq.getRequestDispatcher("/").forward(rq, rs);
    }

    public void handleSignIn (HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String userId = request.getParameter("user-id");
        String pw = request.getParameter("user-pw");
        String email = request.getParameter("email");
        email = "google.com";
        service.create(userId,pw,email);
        log.info(request.getSession().getAttribute("userId"));
        request.getRequestDispatcher("/").forward(request,response);
//        if (service.create(userId, pw, email) == Token.CREATE_SUCESS){
//            response.sendRedirect("/soft");
//        }

    }

    public void handleId (HttpServletRequest request, HttpServletResponse res) throws IOException{
        BufferedReader br = new BufferedReader(request.getReader());
        String query = br.readLine();
        String userId = query.split("=")[1];
        try {
            if (service.check(userId) == Token.ID_OVERLAP) {
                res.getWriter().write("overlap");
            } else {
                res.getWriter().write("free");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void notifyError () {

    }

    public void sendFriendInfo (HttpServletRequest rq, HttpServletResponse rs) throws IOException{
        SoftUser user = service.getUserInfo(rq.getParameter("friendId"));
        String json = mapper.writeValueAsString(user);

//        rs.setContentType("application/json");
        rs.getWriter().write(json);
    }

    public void sendUserInfo (HttpServletRequest request, HttpServletResponse response) {
        String userId = (String) request.getSession().getAttribute("userId");
        SoftUser user = service.getUserInfo(userId);
        log.info(user+"161ChatController");
        try {
            String json = mapper.writeValueAsString(user);
            log.info(json+"cahter");

            response.setContentType("application/json");
            response.getWriter().write(json);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getTemplate (HttpServletRequest rq, HttpServletResponse rs) {

    }

    public void sendUserList (HttpServletRequest rq, HttpServletResponse rs) throws ServletException, IOException {
        HashMap<String, List<SoftUser>> m = new HashMap<String, List<SoftUser>>();
        m.put("users", service.getUserList());
        String json = mapper.writeValueAsString(m);

        rs.setContentType("application/json");
        rs.getWriter().write(json);
    }

    public void sendFriendList (HttpServletRequest rq, HttpServletResponse rs) throws IOException {
        HashMap<String, List<SoftUser>> m = new HashMap<String, List<SoftUser>>();

        String userId = (String) rq.getSession().getAttribute("userId");
        log.info(userId);
        m.put("users", service.getUserList(userId));
        String json = mapper.writeValueAsString(m);

        rs.setContentType("application/json");
        rs.getWriter().write(json);
    }

    public void handleAddFriend (HttpServletRequest rq, HttpServletResponse rs) throws IOException{
        String userId = (String) rq.getSession().getAttribute("userId");
        String friendId = rq.getParameter("friendId");
//        System.out.println(rq.getQueryString());
//        System.out.println(friendId);
//        System.out.println(userId);
//        if (service.addFriend(userId, friendId) == Token.ADD_SUCCESS) {
//            System.out.println("Sueccess");
//            rs.getWriter().write("sucess");
//        }else {
//            rs.getWriter().write("failed");
//        }
        rs.getWriter().write(service.addFriend(userId, friendId).name());
    }

}
