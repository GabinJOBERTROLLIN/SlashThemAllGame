package com.project.RunGame.monsters.model;

import com.project.RunGame.helper.BoundingBox;
import com.project.RunGame.helper.Coordinates;
import com.project.RunGame.helper.SpringContext;
import com.project.RunGame.hero.controller.HeroController;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class MapOfMonster {
    private static final int MAX_MONSTER_ON_MAP = 50;
    private static final double FIELD_OF_VIEW = 50;
    private final AtomicInteger idGenerator = new AtomicInteger(0);
    Map<String, MonsterInMap> monsterMap = new HashMap<String, MonsterInMap>();
    private int MAX_SPREAD_DISTANCE = 5;
    private HeroController heroController;
    public MapOfMonster() {
        this.heroController = SpringContext.getBean(HeroController.class);
    }

    private String createMonster(String monsterName, Coordinates coord) {
        MonsterInMap monster = new MonsterInMap(monsterName, coord);
        String id = Integer.toString(idGenerator.incrementAndGet());

        monsterMap.put(id, monster);
        return id;
    }


    public Map<String, MonsterInMap> getMonsterMap() {
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
        Map<String, MonsterInMap> killedMonsters = new HashMap<String, MonsterInMap>();
        Map<String, MonsterInMap> injuredMonsters = new HashMap<String, MonsterInMap>();

        for (Map.Entry<String, MonsterInMap> monster : monsterMap.entrySet()) {
            Coordinates monsterCenter = monster.getValue().getCoord();
            double size = monster.getValue().getMonster().getSize();
            BoundingBox monsterBoundingBox = new BoundingBox(size, monsterCenter);

            if (hitmapBoundingBox.intersects(monsterBoundingBox)) {
                monstersToRemove.add(monster.getKey());
                killedMonsters.put(monster.getKey(), monster.getValue());

            }
        }
        this.removeMonster(monstersToRemove);
        return new MonsterDamageResult(injuredMonsters, killedMonsters);
    }

    public Map<String, MonsterInMap> generateMonsters(Map<String, Coordinates> heroCoordinates) {
        int NUMBER_OF_MONSTERS = 10;

        Map<String, MonsterInMap> addedMonsters = new HashMap<String, MonsterInMap>();
        if (!heroCoordinates.isEmpty() && this.monsterMap.size() < this.MAX_MONSTER_ON_MAP) {
            List<Coordinates> pointsOutOfView = this.getRandomPointsOutOfView(heroCoordinates, NUMBER_OF_MONSTERS);

            for (Coordinates coord : pointsOutOfView) {
                String id = this.createMonster("Zombie", coord);
                addedMonsters.put(id, this.monsterMap.get(id)); //Probably inneficient
            }
        }
        return addedMonsters;


    }

    public List<Coordinates> getRandomPointsOutOfView(Map<String, Coordinates> heroCoordinates, int numberOfPoints) {
        //Create boundingboxes
        List<BoundingBox> boundingBoxList = new ArrayList<BoundingBox>();


        for (Map.Entry<String, Coordinates> coordinates : heroCoordinates.entrySet()) {
            BoundingBox boundingBox = new BoundingBox(this.FIELD_OF_VIEW, coordinates.getValue());
            boundingBoxList.add(boundingBox);
        }

        //create monsters not in boundingboxes

        List<Coordinates> monsterCoordinates = new ArrayList<Coordinates>();
        while (monsterCoordinates.size() < numberOfPoints) {
            Coordinates newCoord = getRandomEdgePoint(boundingBoxList, this.FIELD_OF_VIEW);
            if (!boundingBoxesContainsCoordinates(boundingBoxList, newCoord)) {
                monsterCoordinates.add(newCoord);
            }
        }
        return monsterCoordinates;
    }

    private Coordinates getRandomEdgePoint(List<BoundingBox> boundingboxes, double FIELD_OF_VIEW) {
        //create random index

        Random random = new Random();
        double randomseconCoordinate = random.nextDouble(FIELD_OF_VIEW);
        int randomIndex = random.nextInt(boundingboxes.size());
        double randomdistance = random.nextDouble(this.MAX_SPREAD_DISTANCE);
        int randomSide = random.nextInt(4);

        BoundingBox chosenBoundingbox = boundingboxes.get(randomIndex);

        if (randomSide == 0) {
            return new Coordinates(chosenBoundingbox.getmaxX() + randomdistance, chosenBoundingbox.getMinY() + randomseconCoordinate);
        } else if (randomSide == 1) {
            return new Coordinates(chosenBoundingbox.getMinX() - randomdistance, chosenBoundingbox.getMinY() + randomseconCoordinate);
        } else if (randomSide == 2) {
            return new Coordinates(chosenBoundingbox.getMinX() + randomseconCoordinate, chosenBoundingbox.getMaxY() + randomdistance);
        } else {
            return new Coordinates(chosenBoundingbox.getMinX() + randomseconCoordinate, chosenBoundingbox.getMinY() - randomdistance);
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

    Coordinates getBestSidewayDirectionCoordinates(MonsterInMap monster, Coordinates heroCoordinates) {
        Coordinates monsterToMove = monster.getCoord();
        double directionDX = (monster.getCoord().getX() - heroCoordinates.getX());
        double directionDY = (monster.getCoord().getY() - heroCoordinates.getY());
        Coordinates direction1 = new Coordinates(monsterToMove.getX() + directionDY, monsterToMove.getY() - directionDX);
        Coordinates direction2 = new Coordinates(monsterToMove.getX() - directionDY, monsterToMove.getY() + directionDX);

        double minDistanceDirection1 = Double.POSITIVE_INFINITY;
        double minDistanceDirection2 = Double.POSITIVE_INFINITY;

        for (Map.Entry<String, MonsterInMap> entry : this.monsterMap.entrySet()) {
            boolean isPointOnline1 = this.isPointOnLineSegment(monsterToMove, direction1, entry.getValue().getCoord(), 1.5);
            if (isPointOnline1 && !monster.equals(entry.getValue())) {
                double distance = monsterToMove.distanceTo(direction1);
                if (distance < minDistanceDirection1) {
                    minDistanceDirection1 = distance;
                }
            }

            boolean isPointOnline2 = this.isPointOnLineSegment(monsterToMove, direction1, entry.getValue().getCoord(), 1.5);
            if (isPointOnline2 && !monster.equals(entry.getValue())) {
                double distance = monsterToMove.distanceTo(direction1);
                if (distance < minDistanceDirection2) {
                    minDistanceDirection2 = distance;
                }
            }
        }
        if (minDistanceDirection1 > minDistanceDirection2) {
            return direction1;
        } else {
            return direction2;
        }
    }

    private boolean isPointOnLineSegment(Coordinates start, Coordinates end, Coordinates point, double margin) {
        double crossProduct = (point.getY() - start.getY()) * (end.getX() - start.getX()) -
                (point.getX() - start.getX()) * (end.getY() - start.getY());

        if (Math.abs(crossProduct) > margin) {
            return false;
        }

        double minX = Math.min(start.getX(), end.getX());
        double maxX = Math.max(start.getX(), end.getX());
        double minY = Math.min(start.getY(), end.getY());
        double maxY = Math.max(start.getY(), end.getY());

        return (point.getX() >= minX && point.getX() <= maxX) &&
                (point.getY() >= minY && point.getY() <= maxY);
    }

    private void damageHeroIfClose(Coordinates heroCoordinates,String userId, MonsterInMap monster){
        if (monster.getCoord().distanceTo(heroCoordinates) < monster.getMonster().getSize()){
            this.heroController.damageHero(userId);
        }
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
                firstHeroId = entry.getKey();
            }
        }
        if (this.canMoveMonster(monster, chosenHeroCoordinates)) {
            this.damageHeroIfClose(chosenHeroCoordinates,firstHeroId,monster);
            return monster.move(chosenHeroCoordinates);
        } else {
            Coordinates direction = this.getBestSidewayDirectionCoordinates(monster, chosenHeroCoordinates);
            if (this.canMoveMonster(monster, direction)) {
                return monster.move(direction);
            }
            return null;

        }

    }


    public boolean canMoveMonster(MonsterInMap monster, Coordinates chosenHeroCoordinates) {
        Coordinates coordMonsterToMoveBasis = monster.getCoord();
        Coordinates coordMonsterToMoveIncrement = monster.getMovement(chosenHeroCoordinates);
        Coordinates coordMonsterToMove = Coordinates.add(coordMonsterToMoveBasis, coordMonsterToMoveIncrement);
        for (Map.Entry<String, MonsterInMap> entry : this.monsterMap.entrySet()) {
            Coordinates coordMonsterIteratedBasis = entry.getValue().getCoord();
            //Coordinates coordMonsterIteratedIncrement = entry.getValue().getMovement(chosenHeroCoordinates);
//Coordinates coordMonsterIterated = Coordinates.add(coordMonsterIteratedBasis, coordMonsterIteratedIncrement);
            Coordinates coordMonsterIterated =coordMonsterIteratedBasis;
            boolean coordMonsterX = Math.abs(coordMonsterIterated.getX() - coordMonsterToMove.getX()) < 1.5;
            boolean coordMonsterY = Math.abs(coordMonsterIterated.getY() - coordMonsterToMove.getY()) < 1.5;
            boolean coordMonster = coordMonsterX && coordMonsterY;

            if (coordMonster && !monster.equals(entry.getValue())) {

                return false;
            }
        }
        return true;
    }

    public Map<String, Coordinates> moveMonsters(Map<String, Coordinates> heroCoordinates) {
        Map<String, Coordinates> newCoord = new HashMap<String, Coordinates>();
        //Map<String,MonsterInMap> blockedMonsters = new HashMap<String,MonsterInMap>();
        for (Map.Entry<String, MonsterInMap> entry : monsterMap.entrySet()) {
            MonsterInMap monster = entry.getValue();
            Coordinates coordMonster = this.moveMonster(heroCoordinates, monster);
            if (coordMonster != null) {
                newCoord.put(entry.getKey(), coordMonster);
            } else {
                //blockedMonsters.put(entry.getKey(),monster);

            }

        }

        return newCoord;
    }
}
