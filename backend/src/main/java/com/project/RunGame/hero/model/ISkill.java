package com.project.RunGame.hero.model;

import com.project.RunGame.helper.Coordinates;
import com.project.RunGame.helper.DirectionEnum;

import java.util.List;

public interface ISkill {

    public List<Coordinates> useSkill(DirectionEnum direction);


}
