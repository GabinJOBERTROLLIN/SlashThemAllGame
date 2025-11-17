package com.project.RunGame.hero.service;

import com.project.RunGame.game.service.GameService;
import com.project.RunGame.helper.Coordinates;
import com.project.RunGame.helper.DirectionEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InputManager {

    GameService gameService;
    UserSessionHeroes userSessionHeroes;

    @Autowired
    public InputManager(GameService gameService, UserSessionHeroes userSessionHeroes) {
        this.gameService = gameService;
        this.userSessionHeroes = userSessionHeroes;
    }


    public boolean handleInput(String skillName, DirectionEnum skillDIrection, String userId) {
        System.out.println("this is input" + skillName);
        this.userSessionHeroes.useSkill(userId, skillName, skillDIrection);
        return false;
    }

    public boolean handleMovement(Coordinates coord, DirectionEnum skillDIrection, String userId) {
        this.userSessionHeroes.moveInput(userId, coord, skillDIrection);
        return false;
    }


}
