package com.project.RunGame.hero.model;

import com.project.RunGame.hero.model.heroType.HeroJohn;

public class HeroFactory {

	public Hero getHero(String heroType) throws Exception {
		if (heroType.equals("John")) {
			return new HeroJohn();
		}
		else {
			throw new Exception();
		}
			
	}
}
