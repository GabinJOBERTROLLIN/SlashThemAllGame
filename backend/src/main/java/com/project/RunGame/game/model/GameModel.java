package com.project.RunGame.game.model;

import com.project.RunGame.dto.GameStartRequest;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity(name = "gameModel")
public class GameModel {

    String heroName;
    @Id
    String gameId;

    public GameModel() {
    }

    public GameModel(GameStartRequest gameStartRequest) {
        this.heroName = gameStartRequest.getHero();
        this.gameId = gameStartRequest.getUserId();
    }

    public String getHeroName() {
        return this.heroName;
    }

    public String getGameId() {
        return this.gameId;
    }

}




