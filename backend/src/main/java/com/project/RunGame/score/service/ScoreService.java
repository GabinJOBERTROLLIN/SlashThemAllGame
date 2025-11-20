package com.project.RunGame.score.service;

import com.project.RunGame.game.controller.websockets.GameWebSocket;
import com.project.RunGame.score.model.Score;
import com.project.RunGame.user.controller.UserController;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class ScoreService {
    UserController userController;
    GameWebSocket gameWebSocket;
    ScoreService(UserController userController, GameWebSocket gameWebSocket){
        this.userController = userController;
        this.gameWebSocket = gameWebSocket;
    }

    Map<String,Score> userScore = new HashMap<>();

    public void putScore(String userId, String name) {
        Score score = new Score(name,0);
        this.userScore.put(userId,score);
    }
    public void updateScore(String userId, int scoreIncrement){
        Score currentScore = this.userScore.get(userId);
        this.userScore.get(userId).setScore(currentScore.getScore() + scoreIncrement);
    }
    public void broadCastScore(String roomId){
        Set<String> userIds = this.userController.getUsersFromRoomId(roomId);
        Set<Score> scores = new HashSet<>();
        for (String userId : userIds){
            if (this.userScore.containsKey(userId)){
                scores.add(this.userScore.get(userId));
            }
        }
        for (String userId : userIds){
            this.gameWebSocket.sendMessageToUser(userId,"scoreBoard",scores);
        }
    }
     public boolean isNameAvailable(String playerName){
        for (Map.Entry<String,Score> entry : userScore.entrySet()){
            boolean entryNameIsPlayerName = entry.getValue().getName().equals(playerName);
            if (entryNameIsPlayerName){
                return false;
            }
        }
        return true;
     }

}
