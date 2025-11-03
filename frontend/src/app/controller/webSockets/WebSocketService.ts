import { Injectable } from "@angular/core";
import { Observable, Subject } from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class WebSocketService {
  private readonly URL = 'ws://localhost:8081/gameWs';
  private webSocketSubject!: WebSocket;
  private messageObserver = new Subject<string>();
  public message: Observable<string> ;
  
  constructor() {
      this.message = this.messageObserver.asObservable();
    
    }
    connect(userId:string): Observable<string> {
      if (this.webSocketSubject) {
      this.webSocketSubject.close();
    }
      const url = `${this.URL}?userId=${userId}`;
      this.webSocketSubject = new WebSocket(url);
      this.webSocketSubject.onopen = (event) => {
      console.log('WebSocket connection opened:', event);
    };
    this.webSocketSubject.onmessage = (event) => {
        this.messageObserver.next(event.data);

    };
    this.webSocketSubject.onclose = (event) => {
      console.log('WebSocket connection closed:', event);
    };
    this.webSocketSubject.onerror = (event) => {
      console.error('WebSocket error:', event);
    }
    return this.messageObserver.asObservable();
    }

    send(data: string): boolean {
  if (this.webSocketSubject.readyState === WebSocket.OPEN) {
    this.webSocketSubject.send(data);
    return true;

  } else {
    console.error('WebSocket is not open. ReadyState:', this.webSocketSubject.readyState);
    return false;}
}
}