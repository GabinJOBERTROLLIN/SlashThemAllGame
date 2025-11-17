package com.project.RunGame.hero.model;


public abstract class AbstractSkill implements ISkill {
    String skillName = "err";

    public String getSkillName() {
        return this.skillName;
    }

}