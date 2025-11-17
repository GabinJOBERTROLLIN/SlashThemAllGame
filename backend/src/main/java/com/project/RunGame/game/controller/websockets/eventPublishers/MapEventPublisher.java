package com.project.RunGame.game.controller.websockets.eventPublishers;

import com.project.RunGame.dto.eventsDto.MapEvent;
import com.project.RunGame.map.model.Coordinates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class MapEventPublisher {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public void publishMapEvent(Set<Coordinates> coordinates, String UserId) {
        MapEvent mapEvent = new MapEvent(this, coordinates, UserId);
        if (!coordinates.isEmpty()) {
            applicationEventPublisher.publishEvent(mapEvent);
        }

    }
}
