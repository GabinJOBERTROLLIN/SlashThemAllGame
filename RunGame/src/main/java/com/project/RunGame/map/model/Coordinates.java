package com.project.RunGame.map.model;

import java.util.Objects;

public class Coordinates {
	int x;
	int y;
	public Coordinates(int x, int y) {
		this.x = x;
		this.y = y;
	}
	public int getX() {
		return this.x;
	}
	public int getY() {
		return this.y;
	}
	
	void setX(int x) {
		this.x = x;
	}
	void setY(int y) {
		this.y = y;
	}
	
	public String toString() {
		return ""+this.getX()+";"+this.getY();
		
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
