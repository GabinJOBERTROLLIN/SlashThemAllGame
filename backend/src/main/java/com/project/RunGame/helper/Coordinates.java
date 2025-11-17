package com.project.RunGame.helper;

import java.util.Objects;

public class Coordinates {
    double x;
    double y;

    public Coordinates(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public static Coordinates add(Coordinates coord1, Coordinates coord2) {
        return new Coordinates(coord1.getX() + coord2.getX(), coord1.getY() + coord2.getY());
    }

    public double getX() {
        return this.x;
    }

    void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return this.y;
    }

    void setY(double y) {
        this.y = y;
    }

    public String toString() {
        return "" + this.getX() + ";" + this.getY();

    }

    public double distanceTo(Coordinates otherCoord) {
        double distX = Math.pow(this.x - otherCoord.getX(), 2);
        double distY = Math.pow(this.y - otherCoord.getY(), 2);
        return Math.sqrt(distX + distY);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Coordinates)) return false;
        Coordinates that = (Coordinates) o;
        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

}
