package com.project.RunGame.map.controller;

import com.project.RunGame.dto.eventsDto.MapEvent;
import com.project.RunGame.map.UserSessionMap.RoomSessionMapService;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class MapEventListener implements ApplicationListener<MapEvent> {
    private final RoomSessionMapService userSessionMapService;

    public MapEventListener(RoomSessionMapService service) {
        this.userSessionMapService = service;
    }

    @Override
    public void onApplicationEvent(MapEvent event) {
        userSessionMapService.updateUserMap(event.getRoomId(), event.getCoordinates());

    }
}
