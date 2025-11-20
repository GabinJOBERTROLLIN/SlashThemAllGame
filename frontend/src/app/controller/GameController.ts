// game.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '../../../environment';

@Injectable({
  providedIn: 'root' 
})
export class GameController{
  private API_URL = environment.apiUrl;
  constructor(private http: HttpClient) {}

  startGame(userId: string, hero: string, playerName:string): Observable<any> {
    return this.http.post(`${environment.apiUrl}/game`, {
      userId,
      hero,
      playerName

    });
  }
  stopGame(userId: string): Observable<any> {
    return this.http.post(`${environment.apiUrl}/game/stopGame?userId=${userId}`, {
      userId
    });
  }
  readyForGame(roomId:string){
    const userId = localStorage.getItem("userId")
    console.log("Notifying server that user is ready for game:",userId);
    return this.http.post(`${environment.apiUrl}/game/ready?roomId=${roomId}&userId=${userId}`, {});

  }
}
