package com.softconn.util;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.server.ServerEndpointConfig;

public class SocketConfigurator extends ServerEndpointConfig.Configurator {


    // Prevent Ovelap session !!!

    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest rq, HandshakeResponse rs){
        HttpSession session = (HttpSession) rq.getHttpSession();
        System.out.println(session.getAttribute("userId"));
        System.out.println("HANDSHAKE");
        sec.getUserProperties().put("httpSession", session);
        sec.getUserProperties().put("servletContext", session.getServletContext());
        sec.getUserProperties().put("userId", (session.getAttribute("userId")));
    }
}
