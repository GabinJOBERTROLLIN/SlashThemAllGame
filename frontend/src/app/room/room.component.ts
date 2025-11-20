import { Component } from '@angular/core';
import { GameController } from '../controller/GameController';
import { KeyWebSocketService } from '../controller/webSockets/KeyWebSocketService';
import { GameComponent } from '../game/game.component';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatDividerModule } from '@angular/material/divider';

@Component({
    selector: 'app-room',
    imports: [GameComponent, CommonModule,MatButtonModule, MatToolbarModule, MatDividerModule],
    templateUrl: './room.component.html',
    styleUrl: './room.component.css'
})
export class RoomComponent {
  private roomId!: string;
  showGame = false;
  gameStarted: boolean = false;
  constructor(private gameContrtoller:GameController, private wsService: KeyWebSocketService,private route: ActivatedRoute) {
    const roomIdStrOrNull = this.route.snapshot.paramMap.get('id');
    if(roomIdStrOrNull){
      this.roomId = roomIdStrOrNull
      localStorage.setItem("roomId",this.roomId.toString())
      
      
    }
    
  }
  toggleGame() {
    this.showGame = !this.showGame;
    if (this.showGame){
      this.startGame();
    }
    else{
      this.stopGame();
    }
  }
  stopGame(){
    const userId: string = String(localStorage.getItem("userId"));
    console.log("this is userId",userId)
    this.gameContrtoller.stopGame(userId).subscribe(response =>{
      console.log("game Finished succesfully",response)
      this.gameStarted = false;
    })
  }
  startGame(){
    this.gameContrtoller.startGame(this.roomId,"John").subscribe(response=>{
      console.log("Game started successfully:",response);
      localStorage.setItem("userId",response)
      const userid = localStorage.getItem("userId");
      if (userid){
        this.gameStarted = true;
        console.log("userId opened ws with",userid);
        this.wsService.openConnection(userid);
      }
    }); }
}
