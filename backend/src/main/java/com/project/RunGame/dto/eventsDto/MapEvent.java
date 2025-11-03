package com.project.RunGame.dto.eventsDto;

import java.util.Set;

import org.springframework.context.ApplicationEvent;

import com.project.RunGame.map.model.Coordinates;

public class MapEvent extends ApplicationEvent {
	private Set<Coordinates> coordinates;
	private String roomId;
	
	public MapEvent(Object source,Set<Coordinates> coordinates,String roomId) {
		super(source);
		this.coordinates = coordinates;
		this.roomId = roomId;
	}
	
	public Set<Coordinates> getCoordinates() {
		return this.coordinates;
	}
	public String getRoomId() {
		return this.roomId;
	}

}
