package com.project.RunGame.monsters.model;

import com.project.RunGame.helper.Coordinates;

public class MonsterInMap {

    private Coordinates coord;
    private Monster monster;

    public MonsterInMap(String monsterName, Coordinates coord) {
        this.coord = coord;
        this.monster = getMonsterFromMonsterType(monsterName);
    }


    private Monster getMonsterFromMonsterType(String monsterName) {
        try {
            return new MonsterFactory().getMonster(monsterName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Coordinates getMovement(Coordinates playerCoordinates) {
        double speed = this.monster.getSpeed();
        double deductionX = playerCoordinates.getX() - this.getCoord().getX();
        double deductionY = playerCoordinates.getY() - this.getCoord().getY();
        double pythaXY = Math.sqrt(Math.pow(deductionX, 2) + Math.pow(deductionY, 2));
        if (pythaXY < 0.1) {
            return new Coordinates(0, 0);
        }
        double x = (deductionX / pythaXY) * speed;
        double y = (deductionY / pythaXY) * speed;
        return new Coordinates(x, y);
    }

    public Coordinates move(Coordinates playerCoordinates) {
        double speed = this.monster.getSpeed();
        double deductionX = playerCoordinates.getX() - this.getCoord().getX();
        double deductionY = playerCoordinates.getY() - this.getCoord().getY();
        double pythaXY = Math.sqrt(Math.pow(deductionX, 2) + Math.pow(deductionY, 2));
        if (pythaXY < 0.1) {
            return new Coordinates(0, 0);
        }
        double x = (deductionX / pythaXY) * speed;
        double y = (deductionY / pythaXY) * speed;
        Coordinates newCoordIncrement = new Coordinates(this.getCoord().getX() + x, this.getCoord().getY() + y);
        this.setCoord(newCoordIncrement);
        return new Coordinates(x, y);
    }

    public Coordinates getCoord() {
        return this.coord;
    }

    public void setCoord(Coordinates coord) {
        this.coord = coord;
    }

    public Monster getMonster() {
        return this.monster;
    }
}
