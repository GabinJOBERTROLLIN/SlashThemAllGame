package com.project.RunGame.game.controller.websockets.eventPublishers;


import com.project.RunGame.dto.eventsDto.MovementEvent;
import com.project.RunGame.helper.Coordinates;
import com.project.RunGame.helper.DirectionEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class MovementEventPublisher {
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public void publishMovementEvent(Coordinates coordinates, String UserId, DirectionEnum direction) {
        MovementEvent movementEvent = new MovementEvent(this, coordinates, direction, UserId);
        applicationEventPublisher.publishEvent(movementEvent);
    }
}
