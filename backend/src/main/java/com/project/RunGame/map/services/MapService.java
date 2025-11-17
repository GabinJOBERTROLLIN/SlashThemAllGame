package com.project.RunGame.map.services;

import com.project.RunGame.game.controller.websockets.GameWebSocket;
import com.project.RunGame.map.model.Coordinates;
import com.project.RunGame.map.services.mapBuilder.OrderBuilderStrategy;
import com.project.RunGame.map.services.mapBuilder.SpreadBuilderStrategy;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

@Service
public class MapService {

    private final OrderBuilderStrategy orderBuilderStrategy;
    private final GameWebSocket gameWebSocket;
    private Map<Coordinates, Integer> map;

    MapService(GameWebSocket gameWebsocket) {
        this.gameWebSocket = gameWebsocket;
        TileFinder tilefinder = new TileFinder();
        this.orderBuilderStrategy = new SpreadBuilderStrategy(tilefinder);

    }

    public Map<Coordinates, Integer> createStartingMap(int mapSize) {
        Map<Coordinates, Integer> map = orderBuilderStrategy.createStartingMap(mapSize);
        this.map = map;
        return this.map;
    }

    public Map<Coordinates, Integer> updateMap(Set<Coordinates> coordinates) {
        Map<Coordinates, Integer> map = orderBuilderStrategy.updateMap(coordinates);
        this.map = map;
        return map;

    }

    public Map<Coordinates, Integer> getMap() {
        return this.map;
    }
}
