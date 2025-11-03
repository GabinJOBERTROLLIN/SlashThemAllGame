// game.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root' 
})
export class GameController{
  constructor(private http: HttpClient) {}

  startGame(userId: string, hero: string): Observable<any> {
    return this.http.post('http://localhost:8081/game', {
      userId,
      hero
    });
  }
  stopGame(userId: string): Observable<any> {
    return this.http.post(`http://localhost:8081/game/stopGame?userId=${userId}`, {
      userId
    });
  }
  readyForGame(roomId:string){
    const userId = localStorage.getItem("userId")
    console.log("Notifying server that user is ready for game:",userId);
    return this.http.post(`http://localhost:8081/game/ready?roomId=${roomId}&userId=${userId}`, {});

  }
}
