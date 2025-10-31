package com.project.RunGame.hero.model;

import java.util.ArrayList;
import java.util.List;

import com.project.RunGame.helper.BoundingBox;
import com.project.RunGame.helper.Coordinates;
import com.project.RunGame.helper.DirectionEnum;

public interface ISkill {
	
	public List<Coordinates> useSkill(DirectionEnum direction);
	
	
}
