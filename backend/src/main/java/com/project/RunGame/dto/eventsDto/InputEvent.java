package com.project.RunGame.dto.eventsDto;

import com.project.RunGame.helper.DirectionEnum;
import org.springframework.context.ApplicationEvent;

public class InputEvent extends ApplicationEvent {
    private String skillName;
    private DirectionEnum direction;
    private String userId;

    public InputEvent(Object source, String message, DirectionEnum direction, String userId) {
        super(source);
        this.skillName = message;
        this.direction = direction;
        this.userId = userId;
    }

    public String getSkillName() {
        return this.skillName;
    }

    public DirectionEnum getDirection() {
        return this.direction;
    }

    public String getUserId() {
        return this.userId;
    }
}
