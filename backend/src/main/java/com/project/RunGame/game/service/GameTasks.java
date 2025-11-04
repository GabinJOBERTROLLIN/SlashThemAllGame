package com.project.RunGame.game.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


public class GameTasks {

	public void generateMonsters(String roomId) {
		RestTemplate restTemplate = new RestTemplate();
		String backendUrl = System.getenv("BACKEND_URL");
		String url = backendUrl + "/monsters/generate?roomId={roomId}";

		Map<String, String> params = new HashMap<>();
		params.put("roomId", roomId);
		restTemplate.postForLocation(url, null,params);
	}

	public void moveMonsters(String roomId) {
		RestTemplate restTemplate = new RestTemplate();
		String backendUrl = System.getenv("BACKEND_URL");
		String url = backendUrl + "/monsters/move?roomId={roomId}";
		Map<String, String> params = new HashMap<>();
		params.put("roomId", roomId);
		restTemplate.postForLocation(url, null,params);	
	}
	
	public void broadcastHeroesCoordinates(String roomId) {
		RestTemplate restTemplate = new RestTemplate();
		String backendUrl = System.getenv("BACKEND_URL");
		String url = backendUrl + "/hero/send?roomId={roomId}";
		Map<String, String> params = new HashMap<>();
		params.put("roomId", roomId);
		restTemplate.postForLocation(url, null,params);	
	
	}
}
