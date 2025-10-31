import { Component, OnDestroy, OnInit } from '@angular/core';
import Phaser from 'phaser';
import { MainScene } from '../scene/MainScene';
import { KeyWebSocketService } from '../controller/webSockets/KeyWebSocketService';
import { GameController } from '../controller/GameController';

@Component({
  selector: 'app-game',
  standalone: true,
  imports: [],
  templateUrl: './game.component.html',
  styleUrl: './game.component.css'
})
export class GameComponent implements OnInit,OnDestroy {
  phaserGame?: Phaser.Game;
  constructor(private ws: KeyWebSocketService, private gameController:GameController) {

  }
  ngOnInit(): void {
    this.phaserGame = new Phaser.Game({
      type: Phaser.AUTO,
      width: 800,
      height: 600,
      scene: []
    });
    const mainScene = new MainScene(this.gameController);
    this.phaserGame.scene.add('MainScene', mainScene, true,{ws:this.ws});
  }
  ngOnDestroy(): void {
    this.phaserGame?.destroy(true);
  }

  
}
