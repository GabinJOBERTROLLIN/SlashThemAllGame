package com.project.RunGame.hero.controller;

import com.project.RunGame.dto.CreateHeroesDto;
import com.project.RunGame.helper.Coordinates;
import com.project.RunGame.hero.service.UserSessionHeroes;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
        for (String heroName : createHeroesDto.getHeroNames()) {
            this.userSessionHeroes.initHeroes(userId, heroName);
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

    @DeleteMapping("hero")
    public boolean deleteHero(@RequestParam String userId){
        return this.userSessionHeroes.deleteHero(userId);
    }

    @PostMapping("/damage")
    public void damageHero(@RequestParam String userId) {
        this.userSessionHeroes.damageHero(userId);
    }
}
