package com.softconn.service;

import com.softconn.model.RoomAccess;
import com.softconn.model.SoftList;
import com.softconn.model.SoftRoom;
import com.softconn.model.UserAcess;
import com.softconn.util.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeneralService {

    private static final RoomAccess access = new RoomAccess();
    private static final UserAcess userAccess = new UserAcess();

    public Map<String ,Object> getList() {
        Map<String, Object> map = new HashMap<>();
        map.put("rooms", access.getAllRoom());
        map.put("listName", "room");
        return map;
    }

    public Token createRoomService (String roomName, String userId) {
        return access.insertRoom(
                SoftRoom.
                Builder.
                createNewRoom(roomName, userId));

    }

    public Token deleteRoomService (String roomId, String masterId) {
        return access.deleteRoom(roomId, masterId);
    }

    public Token updateRoomService (String roomId, String roomName) {
        return access.updateRoom(roomId, roomName);
    }

    public SoftRoom getCurrentRoomInfo (String roomId) {
        return access.getRoomById(roomId);
    }

}
