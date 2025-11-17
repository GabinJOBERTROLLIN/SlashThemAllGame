package com.project.RunGame.map.services.mapBuilder;

import com.project.RunGame.helper.DirectionEnum;
import com.project.RunGame.map.model.BoundingBox;
import com.project.RunGame.map.model.Coordinates;
import com.project.RunGame.map.model.Tile;
import com.project.RunGame.map.services.TileFinder;

import java.util.*;

public class SpreadBuilderStrategy extends OrderBuilderStrategy {

    Set<River> unfinishedRiver = new HashSet<>();

    public SpreadBuilderStrategy(TileFinder tileFinder) {
        super(tileFinder);
    }

    public Map<Coordinates, Integer> updateMap(Set<Coordinates> coordinates) {
        Map<Coordinates, Integer> riverStartingPoint = this.createRiver(coordinates);
        Map<Coordinates, Integer> riverPropagation = this.propagateRivers(coordinates);

        Map<Coordinates, Integer> newTiles = new HashMap<Coordinates, Integer>(riverPropagation);
        newTiles.putAll(riverStartingPoint);

        for (Coordinates coordinate : coordinates) {
            if (!map.containsKey(coordinate)) {
                Map<DirectionEnum, Integer> neighbours = this.getNeighbours(coordinate);
                Tile newTile = this.tileFinder.findTile(neighbours);
                this.map.put(coordinate, newTile.getId());
                newTiles.put(coordinate, newTile.getId());
            }
        }
        return newTiles;
    }


    private Map<Coordinates, Integer> createRiver(Set<Coordinates> coordinates) {
        int CHUNK_SIZE = 10;
        int weightStartRiver = 150;
        Random randomizer = new Random();
        Map<Coordinates, Integer> newTiles = new HashMap<Coordinates, Integer>();
        Iterator<Coordinates> iterator = coordinates.iterator();
        if (randomizer.nextInt(weightStartRiver) == 0 && iterator.hasNext()) {
            System.out.println("create river");
            //create Rivers at starting Point, could be improved
            //Coordinates riverStarTingPoint = coordinates.iterator().next();
            BoundingBox coordinatesBoundingBox = new BoundingBox(coordinates.stream().toList());
            Coordinates riverStarTingPoint = coordinatesBoundingBox.getCenter();
            double occupationRationMin = 1;
            DirectionEnum bestDirection = DirectionEnum.Up;

            for (DirectionEnum direction : DirectionEnum.values()) {
                double occupationRatioDirection = this.getChunkOccupation(riverStarTingPoint, CHUNK_SIZE, direction);
                if (occupationRatioDirection < occupationRationMin) {

                    occupationRationMin = occupationRatioDirection;
                    bestDirection = direction;
                }
            }
            River newRiver = new River(riverStarTingPoint, bestDirection);
            Coordinates nextRiverPoint = newRiver.getNextPoint();
            if (!map.containsKey(nextRiverPoint)) {
                unfinishedRiver.add(newRiver);
                map.put(nextRiverPoint, 34);// 34 for river
                newTiles.put(nextRiverPoint, 34);


            }


        }
        return newTiles;
    }

    private Map<Coordinates, Integer> propagateRivers(Set<Coordinates> coordinates) {
        List<Coordinates> coordinatesSet = new ArrayList<Coordinates>(coordinates);
        BoundingBox areaToUpdate = new BoundingBox(coordinatesSet);

        Map<Coordinates, Integer> newTiles = new HashMap<Coordinates, Integer>();
        List<River> finishedRiver = new ArrayList<>();
        for (River river : this.unfinishedRiver) {
            Coordinates latestPoint = river.getLatestPoint();
            while (areaToUpdate.contains(latestPoint)) {
                latestPoint = river.getNextPoint();
                if (latestPoint == null) {
                    finishedRiver.add(river);
                    break;
                } else {
                    map.put(latestPoint, 34);
                    newTiles.put(latestPoint, 34);
                }

            }
            List<Coordinates> riverCoordinates = river.getRiverCoordinates();
            Map<Coordinates, Integer> riverBankTiles = this.createRiverBank(riverCoordinates, coordinates);////////why this is not called
            newTiles.putAll(riverBankTiles);
        }
        finishedRiver.forEach(this.unfinishedRiver::remove);
        return newTiles;
    }


    private Map<Coordinates, Integer> createRiverBank(List<Coordinates> riverCoordinates, Set<Coordinates> coordinates) {
        Map<Coordinates, Integer> newTiles = new HashMap<>();
        for (Coordinates riverCoordinate : riverCoordinates) {
            for (DirectionEnum direction : DirectionEnum.values()) {
                Coordinates neighbourCoordinate = this.getDirectionNeighbour(riverCoordinate, direction);
                if (!riverCoordinates.contains(neighbourCoordinate) && !this.map.containsKey(neighbourCoordinate)) {
                    Map<DirectionEnum, Integer> tileNeighbours = this.getNeighbours(neighbourCoordinate);
                    Tile tile = this.tileFinder.findTileRiverBank(DirectionEnum.getOpposite(direction), tileNeighbours);

                    if (tile != null) {
                        this.map.put(neighbourCoordinate, tile.getId());
                        newTiles.put(neighbourCoordinate, tile.getId());
                    }

                }
            }
        }
        return newTiles;
    }


    private double getChunkOccupation(Coordinates startingPoint, int chunkSize, DirectionEnum direction) {
        Coordinates chunkCenter = startingPoint;
        if (direction.equals(DirectionEnum.Up)) {
            chunkCenter = new Coordinates(startingPoint.getX(), startingPoint.getY() - chunkSize / 2);
        } else if (direction.equals(DirectionEnum.Down)) {
            chunkCenter = new Coordinates(startingPoint.getX(), startingPoint.getY() + chunkSize / 2);
        } else if (direction.equals(DirectionEnum.Left)) {
            chunkCenter = new Coordinates(startingPoint.getX() - chunkSize / 2, startingPoint.getY());
        } else if (direction.equals(DirectionEnum.Right)) {
            chunkCenter = new Coordinates(startingPoint.getX() + chunkSize / 2, startingPoint.getY());
        }
        BoundingBox chunkBox = new BoundingBox(chunkSize, chunkCenter);
        int openTiles = 0;
        int totalTiles = 0;

        for (int x = chunkBox.getMinX(); x <= chunkBox.getmaxX(); x++) {
            for (int y = chunkBox.getMinY(); y <= chunkBox.getMaxY(); y++) {
                Coordinates coord = new Coordinates(x, y);
                if (!map.containsKey(coord)) { // 34 = water
                    openTiles++;
                }
                totalTiles++;
            }
        }


        return (1 - (double) openTiles / totalTiles);
    }

}
