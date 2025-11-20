package com.project.RunGame.game.service;

import com.project.RunGame.helper.SpringContext;
import com.project.RunGame.hero.controller.HeroController;
import com.project.RunGame.monsters.controller.MonstersController;
import com.project.RunGame.score.controller.ScoreController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;


public class GameTasks {
    //private RestTemplate restTemplate = new RestTemplate();
    private final MonstersController monstersController;
    private final HeroController heroController;
    private final ScoreController scoreController;
    public GameTasks(){
        this.monstersController = SpringContext.getBean(MonstersController.class);
        this.heroController = SpringContext.getBean(HeroController.class);
        this.scoreController = SpringContext.getBean(ScoreController.class);
    }
	public void generateMonsters(String roomId) {
		//String backendUrl = System.getenv("BACKEND_URL");
		//String url = backendUrl + "/monsters/generate?roomId={roomId}";

        //Map<String, String> params = new HashMap<>();
        //params.put("roomId", roomId);
        //this.restTemplate.postForLocation(url, null, params);
        this.monstersController.generateMonsters(roomId);

    }

	public void moveMonsters(String roomId) {
		//String backendUrl = System.getenv("BACKEND_URL");
		//String url = backendUrl + "/monsters/move?roomId={roomId}";
		//Map<String, String> params = new HashMap<>();
		//params.put("roomId", roomId);
		//this.restTemplate.postForLocation(url, null,params);
        this.monstersController.moveMonsters(roomId);
	}
	
	public void broadcastHeroesCoordinates(String roomId) {
		//String backendUrl = System.getenv("BACKEND_URL");
		//String url = backendUrl + "/hero/send?roomId={roomId}";
		//Map<String, String> params = new HashMap<>();
		//params.put("roomId", roomId);
		//this.restTemplate.postForLocation(url, null,params);
        this.heroController.broadcastHeroes(roomId);
	
	}
    public void broadcastScore(String roomId){
        this.scoreController.broadcastScores(roomId);
    }
}
