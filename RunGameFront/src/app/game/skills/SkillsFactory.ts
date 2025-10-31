import * as Phaser from 'phaser';
import { BaseSkill } from './BaseSkill';
import { Slash } from './slash';


export class SkillFactory {
  static createSkill(scene: Phaser.Scene, skillData: any): BaseSkill | null {
 
    switch (skillData) {
      case 'slash':
        return new Slash(scene);

      default:
        console.error(`Unknown skill type: ${skillData.type}`);
        return null;
    }
  }
}
