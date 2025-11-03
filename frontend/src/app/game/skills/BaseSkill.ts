import * as Phaser from 'phaser';
import { Direction } from '../../scene/DirectionEnum';

export abstract class BaseSkill {
  private scene: Phaser.Scene;
  private cooldownTimeMs: number = 1000;
  private lastUsedTime: number = 0;

  constructor(scene: Phaser.Scene) {
    this.scene = scene;
  }
  abstract getAnimationStartAndEnd(direction:string | Direction): {start:number, end:number};
  abstract getName(): string;
  abstract getKey(): string;
  abstract getAssetLink(): string;
  abstract getAssetKey(): string;
  abstract getAnimationKey(): string;

  preload(): void {
    console.log(`Preloading asset: ${this.getAssetKey()} from ${this.getAssetLink()}`);
    this.scene.load.spritesheet(this.getAssetKey(),this.getAssetLink(), {frameWidth: 64,frameHeight: 64});
  }

  
  create(): void {
    for (const direction of Object.values(Direction)) {
    const { start, end } = this.getAnimationStartAndEnd(direction);
      this.scene.anims.create({
          key: this.getAnimationKey() + '-' + direction,
          frames: this.scene.anims.generateFrameNumbers(this.getAssetKey(), { start, end }),
          frameRate: 15,
          repeat: 0,
      });
    }

    }
  
  play(player: Phaser.GameObjects.Sprite,direction:Direction): boolean {
    const currentTime = this.scene.time.now;
    let returnedBool=false;
    if (currentTime - this.lastUsedTime > this.cooldownTimeMs) {
      player.anims.play(this.getAnimationKey()+ '-' + direction, true);
      this.lastUsedTime = currentTime;
      returnedBool=true;
    }
    return returnedBool;
  }
  


}
