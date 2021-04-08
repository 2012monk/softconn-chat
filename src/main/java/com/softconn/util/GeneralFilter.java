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
    private static  String encoding;


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        encoding = filterConfig.getInitParameter("encoding");
//        this.encoding = "UTF-8";

    }

    // CORS VERIFY

    public String[] getUri(HttpServletRequest rq){
        String s = rq.getRequestURI().substring(1);
        return s.split("/");
    }


    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest rq = (HttpServletRequest) servletRequest;
        HttpServletResponse rs = (HttpServletResponse) servletResponse;

        String s = rq.getRequestURI().substring(1);
        String[] rqUri = getUri(rq);
        rq.setCharacterEncoding("UTF-8");
        rs.setCharacterEncoding("UTF-8");


        // info logs
        Enumeration header = rq.getHeaderNames();
        StringBuilder sb = new StringBuilder();

        sb.append("REQ URL : ").append(rq.getRequestURL()).append("\r\n");

        sb.append("IP : ").append(rq.getRemoteAddr()).append("\r\n");
        sb.append("HOST : ").append(rq.getRemoteHost()).append("\r\n");
        sb.append("PORT : ").append(rq.getRemotePort()).append("\r\n");
        sb.append("PROTOCOL : ").append(rq.getProtocol()).append("\r\n");
        sb.append("Scheme : ").append(rq.getScheme()).append("\r\n");

        while (header.hasMoreElements()) {
            String h = (String) header.nextElement();
            sb.append(h).append(":").append(rq.getHeader(h)).append("\r\n");
        }

        log.info(sb.toString());




        String[] valUri = {"main.html", "user", "room", "login" ,"asset"};
        if (SessionHandler.verifySession(rq)){
            log.warn("VERIFIED FROM FILTER");
            // Enter request
            if (rqUri[0].equals("")){

                log.info("VERIFIED MOVE TO MAIN");
                servletRequest.getRequestDispatcher("/main.html").forward(servletRequest, servletResponse);
            }else{
                filterChain.doFilter(servletRequest, servletResponse);

            }
        }else{
//            if (rqUri.length > 2 && rqUri[2].equals("login")){
            if (rqUri[0].equals("login")){

                filterChain.doFilter(servletRequest, servletResponse);

//                servletRequest.getRequestDispatcher("/login").forward(servletRequest, servletResponse);
            }
//            else if (rqUri.length > 2 && rqUri[2].equals("asset")){
            else if (rqUri[0].equals("asset") || rqUri[0].equals("favicon.icon")){
                filterChain.doFilter(servletRequest, servletResponse);
            }
            else{
//                log.warn(Arrays.toString(rqUri));
                servletRequest.getRequestDispatcher("/login").forward(servletRequest, servletResponse);
            }
        }


    }
}
