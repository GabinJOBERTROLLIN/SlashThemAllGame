package com.project.RunGame.map.model;


import java.util.List;

public class BoundingBox {

    private int minX;
    private int maxX;
    private int minY;
    private int maxY;

    public BoundingBox(int size, Coordinates centerCoordinates) {
        int halfSize = size / 2;
        int centerX = centerCoordinates.getX();
        int centerY = centerCoordinates.getY();
        this.minX = centerX - halfSize;
        this.maxX = centerX + halfSize;
        this.minY = centerY - halfSize;
        this.maxY = centerY + halfSize;
    }

    public BoundingBox(List<Coordinates> coordinates) {
        int minX = coordinates.get(0).getX();
        int maxX = coordinates.get(0).getX();
        int minY = coordinates.get(0).getY();
        int maxY = coordinates.get(0).getY();

        for (Coordinates coordinate : coordinates) {
            if (coordinate.getX() <= minX) {
                minX = coordinate.getX();
            } else if (coordinate.getX() >= maxX) {
                maxX = coordinate.getX();
            }
            if (coordinate.getY() <= minY) {
                minY = coordinate.getY();
            } else if (coordinate.getY() >= maxY) {
                maxY = coordinate.getY();
            }
        }
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
    }

    public boolean contains(Coordinates coordinates) {
        int x = coordinates.getX();
        int y = coordinates.getY();
        return (x >= this.minX && x <= this.maxX) && (y >= this.minY && y <= this.maxY);
    }

    public Coordinates getCenter() {
        return new Coordinates(this.minX + this.getXlength() / 2, this.maxY + this.getYlength());
    }

    public boolean intersects(BoundingBox other) {
        System.out.println("trying to intersect" + other + "with " + this);
        return !(this.maxX <= other.minX || this.minX >= other.maxX || this.maxY <= other.minY || this.minY >= other.maxY);
    }

    public int getMinX() {
        return this.minX;
    }

    public int getmaxX() {
        return this.maxX;
    }

    public int getMinY() {
        return this.minY;
    }

    public int getMaxY() {
        return this.maxY;
    }

    public int getXlength() {
        return (this.maxX - this.minX);
    }

    public int getYlength() {
        return (this.maxY - this.minY);
    }

    public String toString() {
        return "maxX" + this.maxX + " minX" + this.minX + " maxY" + this.maxY + " minY" + this.minY;
    }
}

