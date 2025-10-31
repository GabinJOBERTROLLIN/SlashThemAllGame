package com.project.RunGame.map.UserSessionMap;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.project.RunGame.game.controller.websockets.GameWebSocket;
import com.project.RunGame.map.model.Coordinates;
import com.project.RunGame.map.services.MapService;

@Service
public class RoomSessionMapService {
	
	private final MapService mapService;
    private final GameWebSocket gameWebSocket;
    private final Map<String, Map<Coordinates, Integer>> userMaps = new ConcurrentHashMap<>();
    
    
    public RoomSessionMapService(MapService mapService, GameWebSocket gameWebSocket) {
        this.mapService = mapService;
        this.gameWebSocket = gameWebSocket;
    }
    
    public void initUser(String roomId) {
    	if (!userMaps.containsKey(roomId)) {
    		Map<Coordinates, Integer> map = mapService.createStartingMap(45);
            userMaps.put(roomId, map);
    	}
        this.sendMap(roomId, this.userMaps.get(roomId));
    }
    
    public void updateUserMap(String roomId,Set<Coordinates> coordinates) {
    	Map<Coordinates, Integer> map =  mapService.updateMap(coordinates);;
    	userMaps.put(roomId, map);
    	this.sendMap(roomId, map);
    }
    
    public void sendMap(String roomId ,Map<Coordinates,Integer> map) {
		int count = 0;
		int maxChunkSize=50;
		Map<Coordinates,Integer> mapChunk=new HashMap<Coordinates,Integer>();
		for(Map.Entry<Coordinates,Integer> entry:map.entrySet()){
			mapChunk.put(entry.getKey(), entry.getValue());
			count++;
			if (count>=maxChunkSize) {
				this.gameWebSocket.sendMessageToRoom(roomId, "map", mapChunk);
				mapChunk=new HashMap<Coordinates,Integer>();
				count = 0;
			}
		}
		this.gameWebSocket.sendMessageToRoom(roomId,"map", mapChunk);
				
		
	}
    
}
