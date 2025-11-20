package com.project.RunGame.hero.service;

import com.project.RunGame.dto.DamageMonsterDto;
import com.project.RunGame.game.controller.websockets.GameWebSocket;
import com.project.RunGame.helper.Coordinates;
import com.project.RunGame.helper.DirectionEnum;
import com.project.RunGame.hero.model.HeroInMap;
import com.project.RunGame.score.controller.ScoreController;
import com.project.RunGame.user.controller.UserController;
import com.project.RunGame.user.service.RoomSession;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserSessionHeroes {
	private final Map<String, HeroInMap> userHeroes = new ConcurrentHashMap<>();
	private final RoomSession roomSession;
	private final GameWebSocket gameWebSocket;
    private final UserController userController;
    private final ScoreController scoreController;
	private RestTemplate restTemplate = new RestTemplate();

	UserSessionHeroes(RoomSession roomSession,GameWebSocket gameWebSocket, UserController userController, ScoreController scoreController){
		this.roomSession = roomSession;
		this.gameWebSocket = gameWebSocket;
        this.userController = userController;
        this.scoreController = scoreController;
	}
	
	public void initHeroes(String userId, String heroName) {
		HeroInMap heroInMap = new HeroInMap(heroName);
		if (heroInMap.isHeroDefined()) {
			userHeroes.put(userId,heroInMap);
		}
		
	}
	
	public boolean useSkill(String userId, String skillName, DirectionEnum direction) {
		HeroInMap heroInMap = userHeroes.get(userId);
		//if (this.handleMovements(userId,skillName,direction)) {
		//	return true;
		//}
		List<Coordinates> hitmap = heroInMap.useSkill(skillName, direction);
		if (!hitmap.isEmpty()) {
			return this.dealDamages(userId,hitmap);
		}
		return false;
	}
	
	public boolean handleMovements(String userId, String skillName, DirectionEnum direction) {
		Coordinates coord = userHeroes.get(userId).getCoord();
		Coordinates newCoord = new Coordinates(coord.getX(),coord.getY());
		float speed = 2f/16;
		if (skillName.equals("z")){
			newCoord = new Coordinates(coord.getX(),coord.getY()+speed);
		}
		else if (skillName.equals("q")){
			newCoord = new Coordinates(coord.getX()-speed,coord.getY());
		}
		else if (skillName.equals("s")){
			newCoord = new Coordinates(coord.getX(),coord.getY()-speed);
		}
		else if (skillName.equals("d")){
			newCoord = new Coordinates(coord.getX()+speed,coord.getY());
		}
		else {
			return false;
		}
		this.move(userId,newCoord);
		return true;
	};

    private void giveUserKillCount(String userId,int killCount){
        this.gameWebSocket.sendMessageToUser(userId,"kills",killCount);
    }

	public boolean dealDamages(String userId,List<Coordinates> hitmap) {
		String backendUrl = System.getenv("BACKEND_URL");
        String url = backendUrl + "/monsters/damage";
		
		String roomId = this.roomSession.getRoomFromUser(userId);
		DamageMonsterDto damageMonsterDto = new DamageMonsterDto(roomId,1,hitmap);
		HttpEntity<DamageMonsterDto> entity = new HttpEntity<>(damageMonsterDto);
		int numberOfEnemieKilled = this.restTemplate.postForObject(url,entity,int.class);
        if (numberOfEnemieKilled > 0){
            this.giveUserKillCount(userId,numberOfEnemieKilled);
            this.scoreController.updateScore(userId,numberOfEnemieKilled);
        }
		return true;
	}
	
	public void move(String userId, Coordinates coord) {
		HeroInMap heroInMap = userHeroes.get(userId);
		heroInMap.setCoordinates(coord);
	}

	public void moveInput(String userId, Coordinates coord, DirectionEnum skillDIrection) {
		this.move(userId,coord);
		
	}
    public Map<String, Coordinates> getHeroesCoordinates(String roomId) {

		//String backendUrl = System.getenv("BACKEND_URL");
        //String url = backendUrl + "/users/users?roomId={roomId}";

        //Map<String, String> params = new HashMap<>();
        //params.put("roomId", roomId);

        //Set<String> userIds = this.restTemplate.getForObject(url, Set.class, params);
        Set<String> userIds = this.userController.getUsersFromRoomId(roomId);
        Map<String, Coordinates> coordinates = new HashMap<>();

        for (String userId : userIds) {
            HeroInMap hero = this.userHeroes.get(userId);
            coordinates.put(userId, hero.getCoord());

        }
        return coordinates;

    }

    public void sendHeroesCoordinates(String roomId) {
        Map<String, Coordinates> coordinates = getHeroesCoordinates(roomId);
        this.gameWebSocket.sendMessageToRoom(roomId, "heroes", coordinates);
    }


    public boolean deleteHero(String userId) {
        if (this.userHeroes.containsKey(userId)){
            this.userHeroes.remove(userId);
            return true;
        }else{
            return false;
        }
    }

    public void damageHero(String userId) {
        this.scoreController.updateScore(userId,-1);
        this.gameWebSocket.sendMessageToUser(userId,"damageHero",1);

    }
}
