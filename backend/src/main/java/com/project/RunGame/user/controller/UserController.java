package com.project.RunGame.user.controller;

import com.project.RunGame.user.service.RoomSession;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("users")
public class UserController {
    private final RoomSession roomSession;

    UserController(RoomSession roomSession) {
        this.roomSession = roomSession;
    }

    @GetMapping("users")
    public Set<String> getUsersFromRoomId(@RequestParam String roomId) {
        return this.roomSession.getUsersFromRoom(roomId);
    }
    @DeleteMapping("user")
    public boolean deleteUserFromUserId(@RequestParam String userId) {
        return this.roomSession.deleteUser(userId);
    }

    @GetMapping("room")
    public String getRoomFromUserId(@RequestParam String userId) {
        return this.roomSession.getRoomFromUser(userId);
    }


}
