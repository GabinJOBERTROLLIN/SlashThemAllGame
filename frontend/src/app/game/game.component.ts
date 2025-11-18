import { Component, OnDestroy, OnInit } from '@angular/core';
import Phaser from 'phaser';
import { MainScene } from '../scene/MainScene';
import { HeadUpDisplayScene } from '../scene/HeadUpDisplayScene';
import { KeyWebSocketService } from '../controller/webSockets/KeyWebSocketService';
import { GameController } from '../controller/GameController';

@Component({
    selector: 'app-game',
    imports: [],
    templateUrl: './game.component.html',
    styleUrl: './game.component.css'
})
export class GameComponent implements OnInit,OnDestroy {
  phaserGame?: Phaser.Game;
  constructor(private ws: KeyWebSocketService, private gameController:GameController) {

  }
  ngOnInit(): void {
    const container = document.getElementById('phaserContainer') as HTMLElement;
    const initialHeight = container.clientHeight;
    const initialWidth = container.clientWidth;


    this.phaserGame = new Phaser.Game({
      type: Phaser.AUTO,
      parent: 'phaserContainer',
      width: 800,
      height: 600,
      scale: {
        mode: Phaser.Scale.FIT,
        autoCenter: Phaser.Scale.CENTER_BOTH
      },
      scene: []
    });
    const mainScene = new MainScene(this.gameController);
    this.phaserGame.scene.add('MainScene', mainScene, true,{ws:this.ws});
    const hud = new HeadUpDisplayScene();
    this.phaserGame.scene.add('hud', hud, true,{ws:this.ws});
  }
  ngOnDestroy(): void {
    this.phaserGame?.destroy(true);
  }

  
}
