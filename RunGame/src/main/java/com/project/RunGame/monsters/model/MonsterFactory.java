package com.project.RunGame.monsters.model;

import com.project.RunGame.monsters.model.monstersType.Zombie;

public class MonsterFactory {

	public Monster getMonster(String monsterType) throws Exception {
		if (monsterType.equals("Zombie")) {
			return new Zombie();
		}
		else {
			throw new Exception();
		}
			
	}
}