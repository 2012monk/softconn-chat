package com.softconn.controller;

import com.softconn.DB.SessionHandler;
import com.softconn.service.UserService;
import com.softconn.util.Token;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "LoginController", value = "/login/*")
public class LoginController extends HttpServlet {


    private static final UserService service = new UserService();
    private static final SessionHandler handler = new SessionHandler();
    private static final Logger log = LogManager.getLogger(LoginController.class);


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String[] path = getPath(request);
        String path = getPath(request);
        if (path.equals("logout")){
            handleLogOut(request, response);
        }
        else{

            request.getRequestDispatcher("/asset/login.html").include(request, response);
        }
    }



    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String path =getPath(request)[1];
        String path = getPath(request);
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


//
//    public String[] getPath(HttpServletRequest rq){
//        String path = rq.getRequestURI().substring(1);
//        if (path.split("/").length == 1){
//            return new String[2];
//        }
//        return path.split("/");
//    }

    public String getPath(HttpServletRequest rq){
        String path = rq.getRequestURI().substring(1);
        if (path.split("/").length == 1){
            return "";
        }
        else return path.split("/")[1];
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
        rs.sendRedirect("/");
    }

    public void handleSignIn (HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String userId = request.getParameter("user-id");
        String pw = request.getParameter("user-pw");
        String email = request.getParameter("email");
        email = "google.com";
        service.create(userId,pw,email);

//        request.getRequestDispatcher("/").forward(request,response);
        response.sendRedirect("/");

    }

    public void handleId (HttpServletRequest request, HttpServletResponse res) throws IOException{
//        BufferedReader br = new BufferedReader(request.getReader());
//        String query = br.readLine();
//        String userId = query.split("=")[1];
        String userId = request.getParameter("userId");
        log.info(userId);
        try {
            res.getWriter().write(service.check(userId).name());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
