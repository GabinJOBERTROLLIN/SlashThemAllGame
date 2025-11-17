package com.project.RunGame.game.controller.websockets.eventPublishers;

import com.project.RunGame.dto.eventsDto.InputEvent;
import com.project.RunGame.helper.DirectionEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class InputEventPublisher {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public void publishInputEvent(String message, DirectionEnum Direction, String userId) {
        InputEvent inputEvent = new InputEvent(this, message, Direction, userId);
        applicationEventPublisher.publishEvent(inputEvent);
    }
}