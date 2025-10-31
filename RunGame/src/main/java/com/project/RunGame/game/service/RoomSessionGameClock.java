package com.project.RunGame.game.service;

import java.util.HashMap;
import java.util.Map;


public class RoomSessionGameClock {
	Map<String,GameClock> roomGameClock = new HashMap<String, GameClock>();
	
	public void startClock(String roomId) {
		GameClock gameClock = new GameClock(roomId);
		roomGameClock.put(roomId, gameClock);
		gameClock.startClock();
	}
	public void stopClock(String roomId) {
		GameClock gameClock = this.roomGameClock.get(roomId);
		gameClock.stopClock();
	}
}
