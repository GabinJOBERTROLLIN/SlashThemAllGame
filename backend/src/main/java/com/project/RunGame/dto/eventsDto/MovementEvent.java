package com.project.RunGame.dto.eventsDto;

import org.springframework.context.ApplicationEvent;

import com.project.RunGame.helper.Coordinates;
import com.project.RunGame.helper.DirectionEnum;

public class MovementEvent extends ApplicationEvent {
	private Coordinates Coordinates;
	private DirectionEnum direction;
	private String userId;
	public MovementEvent(Object source,Coordinates coord,DirectionEnum direction,String userId ) {
		super(source);
		this.Coordinates=coord;
		this.direction=direction;
		this.userId = userId;
	}
	
	public Coordinates getCoordinates() {
		return this.Coordinates;
	}
	public DirectionEnum getDirection() {
		return this.direction;
	}
	public String getUserId() {
		return this.userId;
		
	}
}
