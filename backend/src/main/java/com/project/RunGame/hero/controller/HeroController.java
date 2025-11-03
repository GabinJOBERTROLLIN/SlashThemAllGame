package com.project.RunGame.hero.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.RunGame.dto.CreateHeroesDto;
import com.project.RunGame.helper.Coordinates;
import com.project.RunGame.hero.service.UserSessionHeroes;

@RestController
@RequestMapping("/hero")
public class HeroController {
	
	private final UserSessionHeroes userSessionHeroes;
	public HeroController(UserSessionHeroes userSessionHeroes) {
		this.userSessionHeroes = userSessionHeroes;
	}
	@PostMapping("/init")
	public void initHeroesUser(@RequestBody CreateHeroesDto createHeroesDto) {
		String userId = createHeroesDto.getUserId();
		for (String heroName: createHeroesDto.getHeroNames()) {
			this.userSessionHeroes.initHeroes(userId,heroName);
		}
	}
	
	@GetMapping("/coordinates")
	public Map<String, Coordinates> getHeroesCoordinates(@RequestParam String roomId) {
		return this.userSessionHeroes.getHeroesCoordinates(roomId);
	}
	
	@PostMapping("/send")
	public void broadcastHeroes(@RequestParam String roomId) {
		this.userSessionHeroes.sendHeroesCoordinates(roomId);
	}
}
