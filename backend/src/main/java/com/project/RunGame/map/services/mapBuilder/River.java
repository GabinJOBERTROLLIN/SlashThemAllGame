package com.project.RunGame.map.services.mapBuilder;


import com.project.RunGame.helper.DirectionEnum;
import com.project.RunGame.map.model.Coordinates;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class River {
    private final Coordinates startingPoint;
    Random randomizer = new Random();
    private DirectionEnum direction = null;
    private Coordinates latestPoint;
    private List<Coordinates> riverCoordinates = new ArrayList<>();

    public River(Coordinates startingPoint, DirectionEnum direction) {
        this.startingPoint = startingPoint;
        this.latestPoint = startingPoint;

        this.direction = direction;
    }

    public River(Coordinates riverStarTingPoint) {
        this.startingPoint = riverStarTingPoint;
        this.latestPoint = riverStarTingPoint;
        DirectionEnum[] directions = DirectionEnum.values();
        this.direction = directions[randomizer.nextInt(directions.length)];
    }

    private int oneOrMinusOne() {
        return randomizer.nextInt(2) * 2 - 1;
    }

    Coordinates getNextPoint() {
        int sideWeight = 10;
        int centerSideWeight = 40;
        int centerWeight = 100;
        int stopWeight = 1;

        int randomIndex = randomizer.nextInt(1, stopWeight + sideWeight + centerSideWeight + centerWeight);

        Coordinates coordChange;
        if (randomIndex <= stopWeight) {
            return null;
        } else if (randomIndex <= sideWeight) {
            int side = this.oneOrMinusOne();
            coordChange = new Coordinates(side, 0);
        } else if (randomIndex <= sideWeight + centerSideWeight) {
            int side = this.oneOrMinusOne();
            coordChange = new Coordinates(side, 1);
        } else {
            coordChange = new Coordinates(1, 0);
        }
        Coordinates rotated = this.changeCoordAccordingToDirection(coordChange);
        Coordinates nextPoint = new Coordinates(rotated.getX() + latestPoint.getX(), rotated.getY() + latestPoint.getY());

        this.riverCoordinates.add(latestPoint); //real latestPoint not in the List
        this.latestPoint = nextPoint;
        return nextPoint;
    }

    private Coordinates changeCoordAccordingToDirection(Coordinates coord) {
        Coordinates rotated;
        int x = coord.getX();
        int y = coord.getY();
        if (this.direction.equals(DirectionEnum.Up)) {
            rotated = new Coordinates(y, -x);
        } else if (this.direction.equals(DirectionEnum.Down)) {
            rotated = new Coordinates(-y, x);
        } else if (this.direction.equals(DirectionEnum.Left)) {
            rotated = new Coordinates(-x, -y);
        } else if (this.direction.equals(DirectionEnum.Right)) {
            rotated = new Coordinates(x, y);
        } else {
            rotated = new Coordinates(x, y);
        }
        return rotated;
    }

    public Coordinates getLatestPoint() {
        return latestPoint;
    }

    public Coordinates getStartingPoint() {
        return startingPoint;
    }

    public DirectionEnum getDirection() {
        return direction;
    }

    public List<Coordinates> getRiverCoordinates() {
        return riverCoordinates;
    }
}
