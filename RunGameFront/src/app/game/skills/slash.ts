import { Direction } from "../../scene/DirectionEnum";
import { BaseSkill } from "./BaseSkill";

export class Slash extends BaseSkill {

    private key = 'A';
    private assetLink = 'assets/John/Sword_attack_with_shadow.png';
    private assetKey = 'slash-asset';
    private animationKey = 'slash-animation';


    constructor(scene: Phaser.Scene) {
    super(scene);
  }
    override getName(): string {
        return 'slash';
    }
    override getKey(): string {
        return this.key;
    }
    override getAssetLink(): string {
        return this.assetLink;
    }
    override getAssetKey(): string {
        return this.assetKey;
    }
    override getAnimationKey(): string {
        return this.animationKey;
    }

    override getAnimationStartAndEnd(direction: string | Direction): { start: number; end: number; } {
        switch (direction) {
            case Direction.Up:
                return { start: 24, end: 31 };
            case Direction.Down:
                return { start: 0, end: 7 };
            case Direction.Left:
                return { start: 8, end: 15 };
            case Direction.Right:
                return { start: 16, end: 23 };
            default:
                return { start: 24, end: 31 }; 
        }
    }

}