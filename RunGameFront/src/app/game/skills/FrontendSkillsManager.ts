import * as Phaser from 'phaser';
import { BaseSkill } from './BaseSkill';
import { SkillFactory } from './SkillsFactory';
import { Direction } from '../../scene/DirectionEnum';

export class FrontendSkillManager {
    private scene: Phaser.Scene;
    private skills: Map<string, BaseSkill>;
    
    constructor(scene: Phaser.Scene) {
        this.scene = scene;
        this.skills = new Map();
    }


    loadSkills(skillDataList: any[]): void {
        skillDataList.forEach(skillData => {
        const skill = SkillFactory.createSkill(this.scene, skillData);
        if (skill) {
            this.skills.set(skill.getKey(), skill);
            console.log(`Preloading skill: ${skill.getKey()} with ${skillData}`);
            skill.preload();
        }
        });
    }


    createAnimations(): void {
        this.skills.forEach(skill => {
        skill.create();
        });
    }


    playSkill(key: string, player: Phaser.GameObjects.Sprite,direction:Direction,isPlayingSkillAnimation?:(response:boolean)=>void): string {
        const skill = this.skills.get(key.toUpperCase());
        let skillName="";
        if (skill) {
            isPlayingSkillAnimation?.(true);
            const wasSkillActivated= skill.play(player,direction);
            if(wasSkillActivated){
                skillName=skill.getName();
            }
             player.once(Phaser.Animations.Events.ANIMATION_COMPLETE, () => {
                isPlayingSkillAnimation?.(false);
        });
        } 
        return skillName;
    }
}
