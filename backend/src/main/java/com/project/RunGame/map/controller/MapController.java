package com.project.RunGame.map.controller;

import com.project.RunGame.map.UserSessionMap.RoomSessionMapService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/map")
public class MapController {
    private final RoomSessionMapService roomSessionMapService;

    public MapController(RoomSessionMapService userSessionMapService) {
        this.roomSessionMapService = userSessionMapService;
    }

    @PostMapping("/init")
    public void initMapUser(@RequestParam String roomId) {
        this.roomSessionMapService.initUser(roomId);
    }
}	
