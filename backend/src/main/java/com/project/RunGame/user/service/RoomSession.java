package com.project.RunGame.user.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RoomSession {
    Map<String, String> userRoom = new ConcurrentHashMap<String, String>();

    public Set<String> getUsersFromRoom(String roomId) {

        Set<String> users = new HashSet<String>();
        for (Map.Entry<String, String> user : userRoom.entrySet()) {
            if (roomId.equals(user.getValue())) {
                users.add(user.getKey());
            }
        }
        return users;
    }

    public String getRoomFromUser(String userId) {
        return this.userRoom.get(userId);
    }

    public void putUser(String userId, String roomId) {
        this.userRoom.put(userId, roomId);
    }


    public void deleteRoom(String roomId) {
        Set<String> userToRemoveId = new HashSet<String>();
        for (Map.Entry<String, String> user : userRoom.entrySet()) {
            if (roomId.equals(user.getValue())) {
                userToRemoveId.add(roomId);
            }
        }
        for (String userId : userToRemoveId) {
            this.userRoom.remove(userId);
        }
    }

    public void removeUserFromRoom(String userId) {
        this.userRoom.remove(userId);
    }

    public boolean isRoomEmpty(String roomId) {
        Set<String> users = getUsersFromRoom(roomId);
        return users.isEmpty();
    }
    public boolean deleteHero(String userId){
        RestTemplate restTemplate = new RestTemplate();
        String backendUrl = System.getenv("BACKEND_URL");
        String url = backendUrl + "/hero/hero?userId=" + userId;
        restTemplate.delete(url);
        return true;
    }
    public boolean deleteUser(String userId) {
        if (this.userRoom.containsKey(userId)){
            this.userRoom.remove(userId);
            return this.deleteHero(userId);
        }
        else{
            return false;
        }

    }
}