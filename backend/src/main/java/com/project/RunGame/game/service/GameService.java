package com.project.RunGame.game.service;

import com.project.RunGame.dto.CreateHeroesDto;
import com.project.RunGame.dto.GameStartRequest;
import com.project.RunGame.game.model.GameModel;
import com.project.RunGame.game.repository.GameRepository;
import com.project.RunGame.user.service.RoomSession;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class GameService {
    private final GameRepository repository;
    private final RoomSession room;
    private final Map<String, RoomSessionGameClock> roomSessionGameClock = new ConcurrentHashMap<String, RoomSessionGameClock>();

    //bad idea, but too lazy to change and good enough for now
    public GameService(GameRepository repository, RoomSession room) {
        this.repository = repository;
        this.room = room;

    }

    public UUID startGame(GameStartRequest gameStartRequest) {
        GameModel gameModel = new GameModel(gameStartRequest);
        UUID uuid = UUID.randomUUID();
        this.repository.save(gameModel);
        return uuid;
    }

    public void readyForMonsters(String userId) {
        RestTemplate restTemplate = new RestTemplate();
        String backendUrl = System.getenv("BACKEND_URL");
        String url = backendUrl + "/monsters/init?roomId={roomId}";

        Map<String, String> params = new HashMap<>();
        params.put("roomId", userId);

        restTemplate.postForLocation(url, null, params);
    }
    public boolean doesRoomExist(String roomId){
        RestTemplate restTemplate = new RestTemplate();
        String backendUrl = System.getenv("BACKEND_URL");
        String url = backendUrl + "/users/users?roomId={roomId}";

        Map<String, String> params = new HashMap<>();
        params.put("roomId", roomId);

        Set<String> userIds = restTemplate.getForObject(url, Set.class, params);
        return !userIds.isEmpty();

    }
    public void readyForUser(String userId, String roomId) {
        RestTemplate restTemplate = new RestTemplate();
        String backendUrl = System.getenv("BACKEND_URL");
        String url = backendUrl + "/hero/init";

        List<String> heroNames = new ArrayList<String>();
        heroNames.add("John");
        CreateHeroesDto createHeroesDto = new CreateHeroesDto(userId, heroNames);
        HttpEntity<CreateHeroesDto> entity = new HttpEntity<>(createHeroesDto);
        restTemplate.postForLocation(url, entity);

        this.room.putUser(userId, roomId);


    }

    public void readyForMap(String userId) {
        RestTemplate restTemplate = new RestTemplate();
        String backendUrl = System.getenv("BACKEND_URL");
        String url = backendUrl +  "/map/init?roomId={roomId}";

        Map<String, String> params = new HashMap<>();
        params.put("roomId", userId);

        restTemplate.postForLocation(url, null, params);
    }

    public void startGameClock(String roomId) {
        RoomSessionGameClock gameClock = new RoomSessionGameClock();
        this.roomSessionGameClock.put(roomId, gameClock);
        gameClock.startClock(roomId);
    }

    public void stopGameClock(String roomId) {
        this.roomSessionGameClock.get(roomId).stopClock(roomId);
    }

    public void stopGameUser(String userId) {
        String roomId = this.room.getRoomFromUser(userId);
        this.room.removeUserFromRoom(userId);
        if (this.room.isRoomEmpty(roomId)) {

            this.stopGameClock(roomId);
        }
    }

}
