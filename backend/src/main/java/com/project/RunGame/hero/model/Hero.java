package com.project.RunGame.hero.model;

import com.project.RunGame.helper.Coordinates;
import com.project.RunGame.helper.DirectionEnum;
import com.project.RunGame.hero.model.skills.Slash;
import com.project.RunGame.hero.model.skills.Walk;

import java.util.ArrayList;
import java.util.List;


public class Hero {
    Integer heroId;
    String pictureArtId = "";
    String aboveArtId = "";

    List<AbstractSkill> skills = new ArrayList<AbstractSkill>();


    public Hero(String pictureArtId, String aboveArtId, Integer heroId) {
        this.pictureArtId = pictureArtId;
        this.aboveArtId = aboveArtId;
        this.heroId = heroId;

        AbstractSkill slash = new Slash();
        AbstractSkill walk = new Walk();

        addSkill(slash);
        addSkill(walk);
    }


    public void addSkill(AbstractSkill skill) {
        this.skills.add(skill);
    }

    public List<Coordinates> useSkill(String skillName, DirectionEnum direction) {
        for (AbstractSkill skill : this.skills) {
            if (skillName.equals(skill.getSkillName())) {
                System.out.println("skil Found in hero");
                return skill.useSkill(direction);
            }
        }
        return new ArrayList<Coordinates>();
    }
}
