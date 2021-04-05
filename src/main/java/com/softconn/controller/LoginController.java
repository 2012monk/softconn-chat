package com.softconn.controller;

import com.softconn.DB.SessionHandler;
import com.softconn.service.UserService;
import com.softconn.util.Token;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;

@WebServlet(name = "LoginController", value = "/login/*")
public class LoginController extends HttpServlet {


    private static final UserService service = new UserService();
    private static final SessionHandler handler = new SessionHandler();
    private static final Logger log = LogManager.getLogger(LoginController.class);


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getRequestURI().split("/")[1];
        if (path.equals("logout")){
            handleLogOut(request, response);
        }
        else{
            request.getRequestDispatcher("/asset/login.html").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getRequestURI().split("/")[3];
        log.warn(path);

        switch (path) {
            case "signin":
                handleSignIn(request, response);
                break;
            case "auth":
                handleLogin(request, response);
                break;
            case "check":
                handleId(request, response);
                break;
        }
    }



    public void handleLogin (HttpServletRequest request, HttpServletResponse res) throws IOException, ServletException {
        String userId = request.getParameter("user-id");
        String pw = request.getParameter("user-pw");




        Token token = service.auth(userId, pw);

        log.info(token);
        if (token == Token.VERIFIED){

            handler.createSession(request, userId);
        }

        res.getWriter().write(token.name());
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
}
