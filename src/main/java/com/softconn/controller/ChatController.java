package com.softconn.controller;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softconn.DB.SoftMessageHandler;
import com.softconn.model.*;
import com.softconn.util.MessageType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@ServerEndpoint(value = "/melt/{type}/{typeId}", configurator = com.softconn.util.SocketConfigurator.class)
public class ChatController {

    private static final Logger log = LogManager.getLogger(ChatController.class);
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final SoftMessageHandler msgHandler = new SoftMessageHandler();
    private static final RoomAccess access = new RoomAccess();
    private static final UserAcess userAccess = new UserAcess();
    private static final ConcurrentHashMap<String, Session> userPool = new ConcurrentHashMap<>();
    private static final HashMap<String, SoftRoom> roomPool = new HashMap<>();
    private String userId;

    static {
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
    }

    public ChatController() {
        init();
    }

    public void init() {

        List<SoftRoom> r = access.getAllRoom();
        for (SoftRoom room : r) {
            roomPool.put(room.getRoomId(), room);
        }
    }


    @OnOpen
    public void onOpen(@PathParam("type") String type,
                       @PathParam("typeId") String recvId,
                       Session session, EndpointConfig config) {
        log.info("connect");
        userId = (String) config.getUserProperties().get("userId");
        log.info(userId);
        if (!userPool.containsKey(userId)) userPool.put(userId, session);
        log.info(userId);
        log.info(userPool.size());
        sendCall(userId, true);
    }

    @OnMessage
    public void onMessage(@PathParam("type") String type,
                          @PathParam("typeId") String recvId,
                          Session session, String reqMsg) {
        log.debug(userId + reqMsg);
        try {
            SoftMessage parsed = mapper.readValue(reqMsg, SoftMessage.class);
            parsed.setSenderId(userId);
            log.info(parsed);
            handleMessage(parsed);

            String json = mapper.writeValueAsString(parsed);
            log.info(json + "PARSED");
        } catch (Exception e) {
//            e.printStackTrace();
            e.printStackTrace();
            log.error(e.getMessage());
        }

    }

    @OnClose
    public void onClose(Session session) {
        String userId = (String) session.getUserProperties().get("userId");
        userPool.remove(userId);
        sendCall(userId, false);

    }

    @OnError
    public void err(Throwable t) {
        t.printStackTrace();

    }

    public void handleMessage(SoftMessage msg) {
        switch (msg.getType()) {
            case ROOM_ENTER:
                handleEnter(msg);
                break;
            case ROOM_MSG:
                roomMsg(msg);
                break;
            case PRIVATE_MSG:
                privateMsg(msg);
                break;
            case ROOM_OUT:
                handleOut(msg);
                break;
        }


    }

    public void handleOut(SoftMessage msg) {
        String userId = msg.getSenderId();
        String roomId = msg.getRoomId();
        if (roomId != null || !roomId.equals("")) {
            roomPool.get(roomId).pullClient(userId);
            sendCall(roomId, userId, roomPool.get(roomId).getCurrentUsers());
            access.deleteRoomUser(roomId, userId);
        }
    }

    public void handleEnter(SoftMessage msg) {
        String userId = msg.getSenderId();
        String roomId = msg.getRoomId();
        roomPool.get(roomId).addClient(userId);
        sendCall(roomId, userId, roomPool.get(roomId).getCurrentUsers());
        access.insertRoomUser(roomId, userId);

    }

    public void privateMsg(SoftMessage msg) {
        try {
            String json = mapper.writeValueAsString(msg);
            if (userPool.containsKey(msg.getReceiverId())) {
                userPool.get(msg.getReceiverId()).getBasicRemote().sendText(json);
                msgHandler.handleMsgLog(msg);
            } else {
                msgHandler.absenceMsgHandle(msg);
            }
            msg.setType(MessageType.SEND_SUCCESS);
            json = mapper.writeValueAsString(msg);
            userPool.get(msg.getSenderId()).getBasicRemote().sendText(json);
        } catch (JsonParseException jp) {
            jp.printStackTrace();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void sendAll(SoftMessage msg) {
        try {
            String json = mapper.writeValueAsString(msg);
            for (Session s : userPool.values()) {
                s.getBasicRemote().sendText(json);
            }
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
    }

    public void roomMsg(SoftMessage msg) {
        String roomId = msg.getRoomId();
        SoftRoom room = roomPool.get(roomId);
        try {
            String json = mapper.writeValueAsString(msg);
            log.info(json);
            for (String s : room.getClientList()) {
                userPool.get(s).getBasicRemote().sendText(json);
            }
        } catch (JsonParseException je) {
            log.warn(je.getMessage());
        } catch (IOException e) {
            log.warn(e.getMessage());
        } finally {
            msgHandler.handleMsgLog(msg);
        }
    }

    public void sendCall(String roomId, String userId, int roomUserCount) {
        ArrayList<String> list = new ArrayList<>(roomPool.get(roomId).getClientList());
        SoftMessage resMsg = new SoftMessage();
        resMsg.setSenderId("server");
        resMsg.setRoomId(roomId);
        resMsg.setType(MessageType.NOTIFY);
        resMsg.setMessage(userId + "  left say bye...");
        resMsg.setUserList(list);
        roomMsg(resMsg);

        NotifyMessage notify = new NotifyMessage();
        notify.setType(MessageType.NOTIFY_ROOM);
        notify.setMessage("roomChange");
        Map<String, Integer> map = new HashMap<>();
        for (SoftRoom r: roomPool.values()){
            map.put(r.getRoomId(), r.getCurrentUsers());
        }
        notify.setList(map);
        try {
            String json = mapper.writeValueAsString(notify);
            for (Session s:userPool.values()){
                s.getBasicRemote().sendText(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void sendCall(String userId, boolean in) {

        List<SoftUser> list = userAccess.friends(userId);
        NotifyMessage msg = new NotifyMessage();
        msg.setUserId(userId);
        if (in) {
            msg.setType(MessageType.NOTIFY_ONLINE);
        } else {
            msg.setType(MessageType.NOTIFY_OFFLINE);
        }

        for (SoftUser s : list) {
            String id = s.getUserId();
            try {
                String json = mapper.writeValueAsString(msg);
                userPool.get(id).getBasicRemote().sendText(json);
            } catch (Exception e) {
                log.warn(e.getMessage());
            }
        }
    }

    public void sendCall() {

    }


}
