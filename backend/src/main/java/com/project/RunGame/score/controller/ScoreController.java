package com.project.RunGame.score.controller;

import com.project.RunGame.score.service.ScoreService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ScoreController {
    private final ScoreService scoreService;

    public ScoreController(ScoreService scoreService){
        this.scoreService = scoreService;
    }
    public void createuser(String userId, String playerName) {
        this.scoreService.putScore(userId,playerName);
    }
    public boolean isNameAvailable(String playerName){
        return this.scoreService.isNameAvailable(playerName);
    }
    public void broadcastScores(String roomId){
        this.scoreService.broadCastScore(roomId);
    }
    public void updateScore(String userId, int scoreIncrement){
        this.scoreService.updateScore(userId, scoreIncrement);
    }
}
