package com.project.RunGame.monsters.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.project.RunGame.dto.DamageMonsterDto;
import com.project.RunGame.helper.Coordinates;
import com.project.RunGame.monsters.service.RoomSessionMonsterService;

@RestController
@RequestMapping("monsters")
public class MonstersController {
	private final RoomSessionMonsterService roomSesisonMonsterService;
	
	public MonstersController(RoomSessionMonsterService roomSesisonMonsterService) {
		this.roomSesisonMonsterService = roomSesisonMonsterService;
		
	}
	
	@PostMapping("/init")
	public void initMonstersUser(@RequestParam String roomId) {
		this.roomSesisonMonsterService.initRoom(roomId);
	}
	
	@PostMapping("/damage")
	public void damageMonster(@RequestBody DamageMonsterDto monsterDto) {
		String roomId = monsterDto.getRoomId(); 
		int damageAmount = monsterDto.getDamageAmount();
		List<Coordinates> hitmap = monsterDto.getHitmap();
		System.out.println("try to damage");
		this.roomSesisonMonsterService.damageMonster(roomId,damageAmount,hitmap);
	}
	@PostMapping("/generate")
	public void generateMonsters(@RequestParam String roomId) {
		this.roomSesisonMonsterService.generateMonsters(roomId);
	}
	
	@PostMapping("/move")
	public void moveMonsters(@RequestParam String roomId) {
		this.roomSesisonMonsterService.moveMonsters(roomId);
	}
}
