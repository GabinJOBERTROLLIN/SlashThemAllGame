package com.project.RunGame.monsters.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import com.project.RunGame.game.controller.websockets.GameWebSocket;
import com.project.RunGame.helper.BoundingBox;
import com.project.RunGame.helper.Coordinates;

public class MapOfMonster {
	Map<String,MonsterInMap> monsterMap = new HashMap<String,MonsterInMap>();
	int MAX_MONSTER_ON_MAP = 50;
	double FIELD_OF_VIEW = 50;
	int MAX_SPREAD_DISTANCE = 5;
	
	private final AtomicInteger idGenerator = new AtomicInteger(0);
	
	public MapOfMonster(){
	}
	
	private  String createMonster(String monsterName,Coordinates coord) {
		MonsterInMap monster = new MonsterInMap(monsterName,coord);
		String id = Integer.toString(idGenerator.incrementAndGet());
		
		monsterMap.put(id, monster);
		return id;
	}

	
	public Map<String,MonsterInMap> getMonsterMap() {
		return this.monsterMap;
	}
	public void removeMonster(List<String> monstersToRemove) {
		for (String monsterID : monstersToRemove) {
			monsterMap.remove(monsterID);	
		}
	}
	public MonsterDamageResult damageMonsters(int damageAmount, List<Coordinates> hitmap) {
		BoundingBox hitmapBoundingBox = new BoundingBox(hitmap);
		List<String> monstersToRemove = new ArrayList<>();
		Map<String,MonsterInMap> killedMonsters = new HashMap<String,MonsterInMap>();
		Map<String, MonsterInMap> injuredMonsters = new HashMap<String,MonsterInMap>();
		for (Map.Entry<String,MonsterInMap> monster: monsterMap.entrySet()) {
			Coordinates monsterCenter = monster.getValue().getCoord();
			double size = monster.getValue().getMonster().getSize();
			BoundingBox monsterBoundingBox = new BoundingBox(size, monsterCenter);
			
			if (hitmapBoundingBox.intersects(monsterBoundingBox)) {
				monstersToRemove.add(monster.getKey());
				killedMonsters.put(monster.getKey(), monster.getValue());
			}
		}
		this.removeMonster(monstersToRemove);
		return new MonsterDamageResult(injuredMonsters,killedMonsters);
	}

	public Map<String, MonsterInMap> generateMonsters(Map<String, Coordinates> heroCoordinates) {
		int NUMBER_OF_MONSTERS = 10;
		
		Map<String, MonsterInMap> addedMonsters = new HashMap<String, MonsterInMap>();
		if (!heroCoordinates.isEmpty() && this.monsterMap.size()< this.MAX_MONSTER_ON_MAP) {
			List<Coordinates> pointsOutOfView = this.getRandomPointsOutOfView(heroCoordinates,NUMBER_OF_MONSTERS);
			
			for (Coordinates coord: pointsOutOfView) {
				String id = this.createMonster("Zombie",coord);
				addedMonsters.put(id, this.monsterMap.get(id)); //Probably inneficient
			}
		}
		return addedMonsters;
	
		
	}
	public List<Coordinates> getRandomPointsOutOfView(Map<String, Coordinates> heroCoordinates, int numberOfPoints){
		//Create boundingboxes
		List<BoundingBox> boundingBoxList = new ArrayList<BoundingBox>(); 
		

		for(Map.Entry<String, Coordinates> coordinates: heroCoordinates.entrySet()) {
			BoundingBox boundingBox = new BoundingBox( this.FIELD_OF_VIEW,coordinates.getValue());
			boundingBoxList.add(boundingBox);
		}
		
		//create monsters not in boundingboxes
		
		List<Coordinates> monsterCoordinates = new ArrayList<Coordinates>();
		while(monsterCoordinates.size() < numberOfPoints) {
			Coordinates newCoord = getRandomEdgePoint(boundingBoxList, this.FIELD_OF_VIEW );
			if (!boundingBoxesContainsCoordinates(boundingBoxList,newCoord)) {
				monsterCoordinates.add(newCoord);
			}
		}
		return monsterCoordinates;
	}
	
	private Coordinates getRandomEdgePoint(List<BoundingBox> boundingboxes,double FIELD_OF_VIEW) {
		//create random index
		
		Random random= new Random();
		double randomseconCoordinate = random.nextDouble(FIELD_OF_VIEW);
		int randomIndex = random.nextInt(boundingboxes.size());
		double randomdistance = random.nextDouble(this.MAX_SPREAD_DISTANCE);
		int randomSide = random.nextInt(4);
		
		BoundingBox chosenBoundingbox = boundingboxes.get(randomIndex);
		
		if (randomSide == 0) {
			return new Coordinates(chosenBoundingbox.getmaxX() + randomdistance , chosenBoundingbox.getMinY() +randomseconCoordinate );
		} else if (randomSide == 1) {
			return new Coordinates(chosenBoundingbox.getMinX() - randomdistance , chosenBoundingbox.getMinY() +randomseconCoordinate );
		}else if (randomSide == 2) {
			return new Coordinates(chosenBoundingbox.getMinX() + randomseconCoordinate , chosenBoundingbox.getMaxY() +randomdistance );
		}else {
			return new Coordinates(chosenBoundingbox.getMinX() + randomseconCoordinate  ,chosenBoundingbox.getMinY() - randomdistance );
		}
				
	}
	
	private boolean boundingBoxesContainsCoordinates(List<BoundingBox> boundingboxes, Coordinates coordinates) {
		for (BoundingBox box : boundingboxes) {
			if (box.contains(coordinates)) {
				return true;
			}
		}
		return false;
	}
	
	public Coordinates moveMonster(Map<String, Coordinates> heroCoordinates, MonsterInMap monster) {
	    Coordinates coordMonster = monster.getCoord();

	    if (heroCoordinates.isEmpty()) {
	        return coordMonster; 
	    }

	    String firstHeroId = heroCoordinates.keySet().iterator().next();
	    Coordinates chosenHeroCoordinates = heroCoordinates.get(firstHeroId);
	    double distanceFirst = chosenHeroCoordinates.distanceTo(coordMonster);

	    for (Map.Entry<String, Coordinates> entry : heroCoordinates.entrySet()) {
	        double distance = entry.getValue().distanceTo(coordMonster);
	        if (distance < distanceFirst) {
	            distanceFirst = distance;
	            chosenHeroCoordinates = entry.getValue();
	        }
	    }
	    return monster.move(chosenHeroCoordinates);
	}

	
	public Map<String, Coordinates> moveMonsters(Map<String, Coordinates> heroCoordinates) {
		Map<String,Coordinates> newCoord = new HashMap<String,Coordinates>();
		for (Map.Entry<String,MonsterInMap> entry: monsterMap.entrySet()) {
			MonsterInMap monster = entry.getValue();
			Coordinates coordMonster = this.moveMonster(heroCoordinates,monster);
			newCoord.put(entry.getKey(), coordMonster);
		}
		return newCoord;
	}
}
