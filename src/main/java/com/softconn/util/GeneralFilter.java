package com.softconn.util;

import com.softconn.DB.SessionHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;

@WebFilter(
        filterName = "GeneralFilter",
        value = "/*")
public class GeneralFilter implements Filter {
    private static final Logger log = LogManager.getLogger(GeneralFilter.class);


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        filterConfig.getInitParameter("encoding");
//        this.encoding = "UTF-8";

    }

    // CORS VERIFY

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest rq = (HttpServletRequest) servletRequest;
        HttpServletResponse rs = (HttpServletResponse) servletResponse;

        String[] rqUri = rq.getRequestURI().split("/");

        rq.setCharacterEncoding("UTF-8");
        rs.setCharacterEncoding("UTF-8");
        log.info(rq.getRequestURI());
//        log.warn(rq.getPathInfo());

        String path = rq.getPathInfo();

        String sessionId = "";
        HttpSession session = null;


//        try {
////            SessionH
//            session = rq.getSession(false);
//            if (session != null){
//                sessionId = (String) session.getAttribute("sessionId");
//            }
//        }catch (Exception e){
//            log.warn(e.getMessage());
//        }
//
//        boolean isLogedIn = session != null && sessionId != null && !sessionId.equals("");
//
//        log.warn(isLogedIn);
        if (SessionHandler.verifySession(rq)){
            // Enter request
            if (rqUri.length < 3){
                log.info(Arrays.toString(rqUri));
                servletRequest.getRequestDispatcher("/main.html").forward(servletRequest, servletResponse);
            }else{
                filterChain.doFilter(servletRequest, servletResponse);

            }
        }else{
            if (rqUri.length > 2 && rqUri[2].equals("login")){
                log.info(Arrays.toString(rqUri));
                filterChain.doFilter(servletRequest, servletResponse);

//                servletRequest.getRequestDispatcher("/login").forward(servletRequest, servletResponse);
            }
            else if (rqUri.length > 2 && rqUri[2].equals("asset")){
                filterChain.doFilter(servletRequest, servletResponse);
            }
            else{
                log.warn(Arrays.toString(rqUri));
                servletRequest.getRequestDispatcher("/login").forward(servletRequest, servletResponse);
            }
        }



//        filterChain.doFilter(servletRequest, servletResponse);
//
//
//            String optionRes = "OPTIONS, GET, POST, HEAD";
//            if (rq.getMethod().equals("OPTION")) {
//                rs.setHeader("Allow", optionRes);
//
//            }
//            if (rq.getHeader("origin") != null) {
//                Enumeration e = rq.getHeaderNames();
//                while (e.hasMoreElements()){
//                    String s = (String) e.nextElement();
//                    log.info(s+"  :  "+rq.getHeader(s));
//                }
////                for (Cookie c: rq.getCo)
////                String url = "http://localhost:47788";
//                String url = "http://112.169.196.76:47788";
//                rs.setHeader("Access-Control-Allow-Credentials", "true");
//                rs.setHeader("Access-Control-Allow-Origin", url);
//                rs.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
//                rs.setHeader("Access-Control-Max-Age", "3600");
//                rs.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, Accept, X-Requested-With, remember-me");
////                rs.setContentType("application/x-www-form-urlencoded");
//                log.warn(rq.getCookies());
//            }
//            filterChain.doFilter(servletRequest, servletResponse);
//        }

//        Enumeration header = rq.getHeaderNames();
//        while (header.hasMoreElements()){
//            String s = (String) header.nextElement();
////            rq.getH
////            System.out.println(s+"  :  "+rq.getHeader(s));
//            String h = "Access-Control-Allow-Origin";
//            if (s.equals("origin")){
//                rs.setHeader(h, "*");
//                rs.setHeader("Access-Control-Allow-Credentials", "true");
//                rs.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
//                rs.setHeader("Access-Control-Max-Age", "3600");
//                rs.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, remember-me");
//            }
//        }

//        System.out.println(servletRequest.getRemoteAddr());
//        System.out.println(servletRequest.getRemotePort()+"PORT!!!");

    }
}
