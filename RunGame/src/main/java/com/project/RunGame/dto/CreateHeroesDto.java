package com.project.RunGame.dto;

import java.util.List;

public class CreateHeroesDto {
	private final String userId;
	private final List<String> heroNames;
	
	public CreateHeroesDto(String userId,List<String> heroNames ) {
		this.userId = userId;
		this.heroNames=heroNames;
	}
	public String getUserId() {
		return this.userId;
	}
	public List<String> getHeroNames(){
		return heroNames;
	}
}
