package com.project.RunGame.hero.controller;

import com.project.RunGame.dto.eventsDto.MovementEvent;
import com.project.RunGame.helper.Coordinates;
import com.project.RunGame.helper.DirectionEnum;
import com.project.RunGame.hero.service.InputManager;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class MovementEventListener implements ApplicationListener<MovementEvent> {
    InputManager inputManager;

    public MovementEventListener(InputManager inputManager) {
        this.inputManager = inputManager;
    }


    @Override
    public void onApplicationEvent(MovementEvent event) {
        Coordinates coord = event.getCoordinates();
        DirectionEnum direction = event.getDirection();
        DirectionEnum skillDirection = event.getDirection();
        String userId = event.getUserId();
        this.inputManager.handleMovement(coord, skillDirection, userId);

    }

}
