package com.project.RunGame.hero.model.skills;

import com.project.RunGame.helper.Coordinates;
import com.project.RunGame.helper.DirectionEnum;
import com.project.RunGame.hero.model.AbstractSkill;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Slash extends AbstractSkill {

    String skillName = "slash";

    List<Coordinates> relativeDistanceHitbox = this.InitializeRelativeDistanceHitbox();

    @Override
    public List<Coordinates> useSkill(DirectionEnum direction) {
        return relativeDistanceHitbox;
    }

    @Override
    public String getSkillName() {
        return this.skillName;
    }

    private List<Coordinates> InitializeRelativeDistanceHitbox() {
        List<Coordinates> relativeDistanceHitbox = new ArrayList<Coordinates>(Arrays.asList(
                new Coordinates(0, -1), new Coordinates(3, -1),
                new Coordinates(0, 1), new Coordinates(3, 1)
        ));
        return relativeDistanceHitbox;

    }


}
