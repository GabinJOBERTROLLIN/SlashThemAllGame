package com.project.RunGame.game.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.RunGame.dto.GameStartRequest;
import com.project.RunGame.game.service.GameService;



@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/game")
public class GameController {
	private final GameService service;
	public GameController(GameService service) {
		this.service = service;
	}
	@PostMapping
	public UUID startGame(@RequestBody GameStartRequest gameStartRequest) {
		System.out.println("Someone asked to start a game ! ");
		return this.service.startGame(gameStartRequest);
	}
	
	@PostMapping("/ready")
	public void readyForMap(@RequestParam String roomId, String userId) {
		System.out.println("ready received userId" + userId);
		this.service.readyForMap(roomId);
		this.service.readyForUser(userId,roomId);
		this.service.readyForMonsters(roomId);
		this.service.startGameClock(roomId);
		
	}
	@PostMapping("stopGame")
	public 	void stopGame(@RequestParam String userId) {
		this.service.stopGameUser(userId);
	}
	
}
