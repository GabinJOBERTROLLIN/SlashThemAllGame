import { Injectable } from "@angular/core";
import { WebSocketService } from "./WebSocketService";
import { Observable, Subject } from "rxjs";
import { Direction } from "../../scene/DirectionEnum";

@Injectable({ providedIn: 'root' })
export class KeyWebSocketService {
    
    
    public message: Observable<string>;
    constructor(private websocketService: WebSocketService) {
        this.message = this.websocketService.message;
    }
    openConnection(userId:string): void {
        console.log("this is the userId send by websocket", userId)
        this.websocketService.connect(userId);
    }

    sendPositionUpdate(arg0: { x: number; y: number; direction: Direction; }):boolean {
        return this.websocketService.send(JSON.stringify({ type: 'position', roomId:localStorage.getItem("userId"), data:arg0 }));
    }
        
    sendKeyPress(skill: string, direction:String): boolean {
        return this.websocketService.send(JSON.stringify({ type: 'skill', roomId:localStorage.getItem("userId"), data:{skill:skill, direction:direction} }));
        
    }
    fetchTiles(tiles:Set<string>): boolean {
        let count :number=0;
        const maxChumnkSize :number= 50;
        const setCHunk:Set<string>=new Set<string>();
        tiles.forEach(tile=>{
            setCHunk.add(tile);
            count++;
            if(count>maxChumnkSize){
                this.websocketService.send(JSON.stringify({ type: 'map', roomId:localStorage.getItem("roomId"),data:{tiles:Array.from(setCHunk)} }));
                count=0;
                setCHunk.clear();
            }});
        return this.websocketService.send(JSON.stringify({ type: 'map', roomId:localStorage.getItem("roomId"), data:{tiles:Array.from(setCHunk)} }));
    }
}