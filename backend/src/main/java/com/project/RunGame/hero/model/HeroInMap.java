package com.project.RunGame.hero.model;

import java.util.ArrayList;
import java.util.List;

import com.project.RunGame.helper.Coordinates;
import com.project.RunGame.helper.DirectionEnum;

public class HeroInMap {
	private Coordinates coord;
	private Hero hero = null;
	
	public HeroInMap(String heroType,Coordinates coord) {
		this.hero = this.getHeroFromHeroType(heroType);
		this.coord = coord;
	}
	public HeroInMap(String heroType) {
		this.hero = this.getHeroFromHeroType(heroType);
		this.coord = new Coordinates(400/16,300/16);
		//TODO : Change those value, for instance set them to 1 and adjust the frontend accordingly
	}
	public boolean isHeroDefined() {
		return this.hero != null;
	}
	public List<Coordinates> adaptRelativeDistanceHitboxToDirection(DirectionEnum skillDdirection,List<Coordinates> relativeDistanceHitbox) {
		List<Coordinates> rotatedHitbox = new ArrayList<>();
		for (Coordinates coord : relativeDistanceHitbox) {
		   	double x = coord.getX();
		   	double y = coord.getY();
			Coordinates rotated;
			
			if (skillDdirection.equals(DirectionEnum.Up)) {
				rotated = new Coordinates(y, -x);
			}
			else if(skillDdirection.equals(DirectionEnum.Down)) {
				rotated = new Coordinates(-y, x);
			}
			else if(skillDdirection.equals(DirectionEnum.Left)) {
				rotated = new Coordinates(-x, -y);
			}
			else if(skillDdirection.equals(DirectionEnum.Right)) {
				rotated = new Coordinates(x, y);
			}
			else {
				rotated = new Coordinates(x, y);
			}
			rotatedHitbox.add(rotated);
		   }
		 return rotatedHitbox;
	}
	
	List<Coordinates> calculateHitMap(List<Coordinates> relativeDistanceHitbox,Coordinates skillStartingPosition){
		List<Coordinates> hitMap = new ArrayList<Coordinates>();
		for (Coordinates coordinate: relativeDistanceHitbox) {
			double x = coordinate.getX()+skillStartingPosition.getX();
			double y = coordinate.getY()+skillStartingPosition.getY();
			Coordinates newCoord = new Coordinates(x,y);
			hitMap.add(newCoord);
		}
		return hitMap; 
	}
	
	public List<Coordinates> useSkill(String skillName,DirectionEnum direction) {
		List<Coordinates> directionRelative = this.getHero().useSkill(skillName, direction);
		List<Coordinates> rotatedDirectionRelative = this.adaptRelativeDistanceHitboxToDirection(direction, directionRelative);
		List<Coordinates> hitMap = this.calculateHitMap(rotatedDirectionRelative, this.coord);
		return hitMap;
	}
	
	
	
	
	public Hero getHero() {
		return this.hero;
	}
	public Coordinates getCoord() {
		return this.coord;
	}
	public void setCoordinates(Coordinates coord) {
		this.coord=coord;
	}
	private Hero getHeroFromHeroType(String heroType) {
		try {
			return  new HeroFactory().getHero(heroType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
}
