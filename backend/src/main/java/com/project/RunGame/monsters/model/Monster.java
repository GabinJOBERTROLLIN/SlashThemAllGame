package com.project.RunGame.monsters.model;

import com.project.RunGame.helper.Coordinates;

public class Monster {
	private String name = "DefaultMonster";
	private double size = (double) 2;
	private double speed = 0.25;
	protected Monster(){
	}

	public String getName() {
		return this.name;
	}
	public double getSize() {
		return this.size;
	}
	public double getSpeed() {
		return this.speed;
	}
}
