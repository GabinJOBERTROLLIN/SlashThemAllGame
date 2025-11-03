package com.project.RunGame.dto;

import com.project.RunGame.helper.Coordinates;
import com.project.RunGame.monsters.model.Monster;
import com.project.RunGame.monsters.model.MonsterStatusEnum;

public class MonsterDto {
	String coord;
	String monsterName;
	MonsterStatusEnum status = MonsterStatusEnum.Alive;
	
	public MonsterDto(Coordinates coord,Monster monster){
	this.monsterName = monster.getName();
	this.coord = coord.toString();
	 
	}
	public MonsterDto(Coordinates coord,Monster monster, MonsterStatusEnum status){
		this.monsterName = monster.getName();
		this.coord = coord.toString();
		this.status = status;
		}
	 
	public String getCoord() {
		return this.coord;
	}
	public String getMonsterName() {
		return this.monsterName;
	}
	public MonsterStatusEnum getStatus() {
		return this.status;
	}
}
