package com.project.RunGame.hero.controller;

import com.project.RunGame.dto.eventsDto.InputEvent;
import com.project.RunGame.helper.DirectionEnum;
import com.project.RunGame.hero.service.InputManager;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class InputEventListener implements ApplicationListener<InputEvent> {
    InputManager inputManager;

    public InputEventListener(InputManager inputManager) {
        this.inputManager = inputManager;
    }


    @Override
    public void onApplicationEvent(InputEvent event) {
        String skillName = event.getSkillName();
        DirectionEnum skillDirection = event.getDirection();
        String userId = event.getUserId();
        this.inputManager.handleInput(skillName, skillDirection, userId);

    }

}
