package com.project.RunGame.game.controller.websockets.eventPublishers;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.project.RunGame.dto.eventsDto.MapEvent;
import com.project.RunGame.map.model.Coordinates;

@Component
public class MapEventPublisher {
	
	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;
	
	public void publishMapEvent(Set<Coordinates> coordinates, String UserId) {
		MapEvent mapEvent = new MapEvent(this,coordinates,UserId);
		if (!coordinates.isEmpty()) {
			applicationEventPublisher.publishEvent(mapEvent);
		}
		
	}
}
