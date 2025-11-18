package com.project.RunGame.map.services.mapBuilder;

import com.project.RunGame.helper.DirectionEnum;
import com.project.RunGame.map.model.Coordinates;
import com.project.RunGame.map.model.Tile;
import com.project.RunGame.map.services.TileFinder;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class OrderBuilderStrategy extends AbstractMapBuilderStrategy {
    Map<Coordinates, Integer> map;

    public OrderBuilderStrategy(TileFinder tileFinder) {
        super(tileFinder);
        this.map = new HashMap<Coordinates, Integer>();

    }

    @Override
    public Map<Coordinates, Integer> createStartingMap(int mapSize) {
        System.out.println("Initialising map");
        this.map = new HashMap<Coordinates, Integer>();
        Tile tile = this.tileFinder.getRandomTile();
        this.map.put(new Coordinates(-1, 0), tile.getId());
        for (int x = 0; x < mapSize; x++) {
            for (int y = 0; y < mapSize; y++) {
                Coordinates coord = new Coordinates(x, y);
                Map<DirectionEnum, Integer> neighbours = getNeighbours(coord);

                Tile newTile = this.tileFinder.findTile(neighbours);
                this.map.put(coord, newTile.getId());

            }
        }
        System.out.println(this.map.toString());
        return this.map;
    }

    public Map<Coordinates, Integer> updateMap(Set<Coordinates> coordinates) {
        Map<Coordinates, Integer> newTiles = new HashMap<Coordinates, Integer>();
        for (Coordinates coordinate : coordinates) {
            Map<DirectionEnum, Integer> neighbours = getNeighbours(coordinate);
            Tile newTile = this.tileFinder.findTile(neighbours);
            this.map.put(coordinate, newTile.getId());
            newTiles.put(coordinate, newTile.getId());
        }
        return newTiles;

    }

    Map<DirectionEnum, Integer> getNeighbours(Coordinates coord) {
        Map<DirectionEnum, Integer> neighbours = new HashMap<DirectionEnum, Integer>();

        for (DirectionEnum direction : DirectionEnum.values()) {
            Coordinates coordNeighbour = getDirectionNeighbour(coord, direction);
            int neighbour;
            neighbour = this.map.getOrDefault(coordNeighbour, -1);

            if (neighbour != -1) {
                neighbours.put(direction, neighbour);
            }

        }
        return neighbours;
    }

    protected Coordinates getDirectionNeighbour(Coordinates coord, DirectionEnum direction) {
        Coordinates coordNeighbour = null;

        if (direction == DirectionEnum.Up) {
            coordNeighbour = new Coordinates(coord.getX(), coord.getY() - 1);
        } else if (direction == DirectionEnum.Down) {
            coordNeighbour = new Coordinates(coord.getX(), coord.getY() + 1);
        } else if (direction == DirectionEnum.Left) {
            coordNeighbour = new Coordinates(coord.getX() - 1, coord.getY());
        } else if (direction == DirectionEnum.Right) {
            coordNeighbour = new Coordinates(coord.getX() + 1, coord.getY());
        } else if (direction == DirectionEnum.UpLeft) {
            coordNeighbour = new Coordinates(coord.getX() - 1, coord.getY() - 1);
        } else if (direction == DirectionEnum.UpRight) {
            coordNeighbour = new Coordinates(coord.getX() + 1, coord.getY() - 1);
        } else if (direction == DirectionEnum.DownLeft) {
            coordNeighbour = new Coordinates(coord.getX() - 1, coord.getY() + 1);
        } else if (direction == DirectionEnum.DownRight) {
            coordNeighbour = new Coordinates(coord.getX() + 1, coord.getY() + 1);
        }
        return coordNeighbour;
    }

}
