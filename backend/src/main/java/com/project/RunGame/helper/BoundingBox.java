package com.project.RunGame.helper;

import java.util.List;

public class BoundingBox {

    private double minX;
    private double maxX;
    private double minY;
    private double maxY;


    public BoundingBox(double size, Coordinates centerCoordinates) {
        double halfSize = size / 2;
        double centerX = centerCoordinates.getX();
        double centerY = centerCoordinates.getY();
        this.minX = centerX - halfSize;
        this.maxX = centerX + halfSize;
        this.minY = centerY - halfSize;
        this.maxY = centerY + halfSize;
    }

    public BoundingBox(List<Coordinates> coordinates) {
        double minX = coordinates.get(0).getX();
        double maxX = coordinates.get(0).getX();
        double minY = coordinates.get(0).getY();
        double maxY = coordinates.get(0).getY();

        for (Coordinates coordinate : coordinates) {
            if (coordinate.getX() < minX) {
                minX = coordinate.getX();
            } else if (coordinate.getX() > maxX) {
                maxX = coordinate.getX();
            }
            if (coordinate.getY() < minY) {
                minY = coordinate.getY();
            } else if (coordinate.getY() > maxY) {
                maxY = coordinate.getY();
            }
        }
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;

    }

    public boolean contains(Coordinates coordinates) {
        double x = coordinates.getX();
        double y = coordinates.getY();
        return (x > this.minX && x < this.maxX) && (y > this.minY && y < this.maxY);
    }

    public boolean intersects(BoundingBox other) {
        System.out.println("trying to intersect" + other + "with " + this);
        return !(this.maxX < other.minX || this.minX > other.maxX || this.maxY < other.minY || this.minY > other.maxY);
    }

    public double getMinX() {
        return this.minX;
    }

    public double getmaxX() {
        return this.maxX;
    }

    public double getMinY() {
        return this.minY;
    }

    public double getMaxY() {
        return this.maxY;
    }

    public String toString() {
        return "maxX" + this.maxX + " minX" + this.minX + " maxY" + this.maxY + " minY" + this.minY;
    }
}
