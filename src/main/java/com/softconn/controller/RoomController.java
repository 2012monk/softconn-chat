package com.softconn.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softconn.model.RoomAccess;
import com.softconn.model.SoftRoom;
import com.softconn.service.GeneralService;
import com.softconn.util.Token;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.Arrays;

@WebServlet(name = "RoomController", value = "/room/*")
public class RoomController extends HttpServlet {


    private static final GeneralService service = new GeneralService();
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final Logger log = LogManager.getLogger(RoomController.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
//        String[] path = request.getPathInfo().split("/");
//        log.info(Arrays.toString(path));
        String path = getPath(request);
        switch (path){
            case "list":
                sendRoomList(response);
                break;
            case "info":
                sendRoomInfo(request, response);
                break;

        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String[] path = request.getPathInfo().split("/");
        log.info(request.getParameter("roomName"));
        String path = getPath(request);
        log.info(path);
        switch (path){
            case "create":
                createRoomHandle(request,response);
                break;
            case "delete":
                deleteRoomHandle(request,response);
                break;
            case "update":
                updateRoomHandle(request, response);
                break;
        }
    }

    public String getPath(HttpServletRequest rq){
        String path = rq.getRequestURI().substring(1);
        if (path.split("/").length == 1){
            return "";
        }
        else return path.split("/")[1];
    }

    public void sendRoomInfo (HttpServletRequest rq, HttpServletResponse rs) {
        String roomId = rq.getParameter("roomId");
        SoftRoom room = service.getCurrentRoomInfo(roomId);
        try {
            String json = mapper.writeValueAsString(room);
            log.warn(json);
            rs.getWriter().write(json);
        } catch (Exception e) {
            log.warn(e.getMessage());
        }

    }

    public void sendRoomList (HttpServletResponse rs) {
        log.info("sendRoomList Works");
        try {
            String json = mapper.writeValueAsString(service.getList());
//            log.debug(json);

            rs.getWriter().write(json);
        }catch (Exception e) {
            log.warn(e.getMessage());
            e.printStackTrace();
        }
    }

    public void createRoomHandle (HttpServletRequest rq, HttpServletResponse rs) throws IOException, ServletException{
        String roomName = rq.getParameter("roomName");
        String userId = (String) rq.getSession().getAttribute("userId");
        log.info(roomName);
        rs.getWriter().write(service.createRoomService(roomName, userId).name());
    }

    public void deleteRoomHandle (HttpServletRequest rq, HttpServletResponse rs) throws IOException, ServletException {
        String roomId = rq.getParameter("roomId");
        String masterId = (String) rq.getSession().getAttribute("userId");
        log.info(roomId, masterId);
        log.info(service.deleteRoomService(roomId, masterId).name());
//        rq.getRequestDispatcher("/").forward(rq, rs);
        rs.sendRedirect("/");
    }

    public void updateRoomHandle (HttpServletRequest rq, HttpServletResponse rs) throws IOException, ServletException {
        String roomId = rq.getParameter("roomId");
        String roomName = rq.getParameter("roomName");
        log.info(roomName, roomId);
        rs.getWriter().write(service.updateRoomService(roomId, roomName).name());
    }




}
