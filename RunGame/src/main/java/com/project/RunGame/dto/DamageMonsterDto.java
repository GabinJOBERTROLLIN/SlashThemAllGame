package com.project.RunGame.dto;

import java.util.List;

import com.project.RunGame.helper.Coordinates;

public class DamageMonsterDto {
	private final String roomId;
	private final int damageAmount;
	private final List<Coordinates> hitmap;

	
	public DamageMonsterDto(String roomId,int damageAmount,List<Coordinates> hitmap) {
		this.roomId = roomId;
		this.damageAmount = damageAmount;
		this.hitmap = hitmap;

	}
	
	public String getRoomId() {
		return this.roomId;
	}
	public int getDamageAmount() {
		return this.damageAmount;
	}
	public List<Coordinates> getHitmap(){
		return this.hitmap;
	}

}
