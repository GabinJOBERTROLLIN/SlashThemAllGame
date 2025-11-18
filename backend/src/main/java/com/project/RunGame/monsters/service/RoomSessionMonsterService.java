package com.project.RunGame.monsters.service;

import com.project.RunGame.dto.MonsterDto;
import com.project.RunGame.game.controller.websockets.GameWebSocket;
import com.project.RunGame.helper.Coordinates;
import com.project.RunGame.monsters.model.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RoomSessionMonsterService {

    private final Map<String, MapOfMonster> roomMonsters = new HashMap<>();
    private GameWebSocket gameWebSocket;

    public RoomSessionMonsterService(GameWebSocket gameWebSocket) {
        this.gameWebSocket = gameWebSocket;
    }

    public void initRoom(String roomId) {
        if (!this.roomMonsters.containsKey(roomId)) {
            MapOfMonster monsters = new MapOfMonster();
            this.roomMonsters.put(roomId, monsters);
        } else {
            System.out.println("room Monsters already initialized");
        }
        MapOfMonster monsters = this.roomMonsters.get(roomId);
        this.sendMonsters(roomId, monsters.getMonsterMap());
    }

    public void damageMonster(String roomId, int damageAmount, List<Coordinates> hitmap) {
        MonsterDamageResult updatedMonsters = roomMonsters.get(roomId).damageMonsters(damageAmount, hitmap);
        Map<String, MonsterInMap> injuredMonsters = updatedMonsters.getInjuredMonsters();
        Map<String, MonsterInMap> killedMonsters = updatedMonsters.getKilledMonsters();
        if (!injuredMonsters.isEmpty()) {
            this.sendMonsters(roomId, injuredMonsters, MonsterStatusEnum.Injured);
        }
        if (!killedMonsters.isEmpty()) {
            this.sendMonsters(roomId, killedMonsters, MonsterStatusEnum.Dead);
        }
    }

    public void sendMonsters(String roomId, Map<String, MonsterInMap> monsters) {
        this.sendMonsters(roomId, monsters, MonsterStatusEnum.Alive);
    }

    public void sendMonsters(String roomId, Map<String, MonsterInMap> monsters, MonsterStatusEnum monsterStatus) {
        int count = 0;
        int maxChunkSize = 50;
        Map<String, MonsterDto> monstersChunk = new HashMap<String, MonsterDto>();

        for (Map.Entry<String, MonsterInMap> entry : monsters.entrySet()) {
            Coordinates coord = entry.getValue().getCoord();
            Monster monster = entry.getValue().getMonster();
            MonsterDto monsterDto = new MonsterDto(coord, monster, monsterStatus);

            monstersChunk.put(entry.getKey(), monsterDto);
            count++;

            if (count >= maxChunkSize) {
                this.gameWebSocket.sendMessageToRoom(roomId, "monsters", monstersChunk);
                monstersChunk = new HashMap<String, MonsterDto>();
                count = 0;
            }
        }
        this.gameWebSocket.sendMessageToRoom(roomId, "monsters", monstersChunk);
    }

    private void sendMoveMonsters(String roomId, Map<String, Coordinates> coordinatesMovement) {
        int count = 0;
        int maxChunkSize = 50;
        Map<String, Coordinates> monstersChunk = new HashMap<String, Coordinates>();

        for (Map.Entry<String, Coordinates> entry : coordinatesMovement.entrySet()) {
            Coordinates coord = entry.getValue();
            String monsterId = entry.getKey();


            monstersChunk.put(monsterId, coord);
            count++;

            if (count >= maxChunkSize) {
                this.gameWebSocket.sendMessageToRoom(roomId, "monstersMove", monstersChunk);
                monstersChunk = new HashMap<String, Coordinates>();
                count = 0;
            }
        }
        this.gameWebSocket.sendMessageToRoom(roomId, "monstersMove", monstersChunk);
    }

    public void generateMonsters(String roomId) {
        MapOfMonster monsters = this.roomMonsters.get(roomId);
        Map<String, Coordinates> heroCoordinates = this.getHeroCoordinates(roomId);

        Map<String, MonsterInMap> monsterMap = monsters.generateMonsters(heroCoordinates);
        this.sendMonsters(roomId, monsterMap);
    }

    public void moveMonsters(String roomId) {
        MapOfMonster monsters = this.roomMonsters.get(roomId);
        Map<String, Coordinates> heroCoordinates = this.getHeroCoordinates(roomId);
        Map<String, Coordinates> newCoords = monsters.moveMonsters(heroCoordinates);
        this.sendMoveMonsters(roomId, newCoords);
    }

    private Map<String, Coordinates> getHeroCoordinates(String roomId) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8081/hero/coordinates?roomId={roomId}";

        Map<String, String> params = new HashMap<>();
        params.put("roomId", roomId);

        try {

            Map<String, Map<String, Object>> rawResponse = restTemplate.getForObject(url, Map.class, params);
            Map<String, Coordinates> result = new HashMap<>();

            if (rawResponse != null) {
                for (Map.Entry<String, Map<String, Object>> entry : rawResponse.entrySet()) {
                    String heroId = entry.getKey();
                    Map<String, Object> coordMap = entry.getValue();

                    double x = ((Number) coordMap.get("x")).doubleValue();
                    double y = ((Number) coordMap.get("y")).doubleValue();

                    result.put(heroId, new Coordinates(x, y));
                }
            }

            return result;


        } catch (Exception e) {
            System.err.println("Failed to fetch hero coordinates for room " + roomId + ": " + e.getMessage());
            return Collections.emptyMap();
        }
    }
}
