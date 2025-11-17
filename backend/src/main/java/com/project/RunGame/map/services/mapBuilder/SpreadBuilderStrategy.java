package com.project.RunGame.map.services.mapBuilder;

import com.project.RunGame.helper.DirectionEnum;
import com.project.RunGame.map.model.Coordinates;
import com.project.RunGame.map.model.Tile;
import com.project.RunGame.map.services.TileFinder;

import java.util.*;

public class SpreadBuilderFactory  extends OrderBuilderStrategy{
    Map<Coordinates, Integer> map;
    Set<River> unfinishedRiver;
    public SpreadBuilderFactory(TileFinder tileFinder) {
        super(tileFinder);
    }

    public Map<Coordinates,Integer> updateMap(Set<Coordinates> coordinates){
        Map<Coordinates,Integer> newTiles = new HashMap<Coordinates, Integer>();
        for(Coordinates coordinate : coordinates) {
            Map<DirectionEnum,Integer> neighbours = this.getNeighbours(coordinate);
            Tile newTile = this.tileFinder.findTile(neighbours);
            this.map.put(coordinate, newTile.getId());
            newTiles.put(coordinate, newTile.getId());
        }
        return  newTiles;

    }
    private void createRiver(Set<Coordinates> coordinates){
        int weightStartRiver = 100;
        Random randomizer = new Random();
        Iterator<Coordinates> iterator = coordinates.iterator();
        if (randomizer.nextInt(weightStartRiver) ==0 && iterator.hasNext()){
            Coordinates riverStarTingPoint = coordinates.iterator().next();

            River newRiver = new River(riverStarTingPoint);
        }

    }
}
