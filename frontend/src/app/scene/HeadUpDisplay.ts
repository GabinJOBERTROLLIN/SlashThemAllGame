import { KeyWebSocketService } from "../controller/webSockets/KeyWebSocketService";

export class HeadUpDisplay {
    private scene: Phaser.Scene;
    private score: number = 0;
    constructor(scene: Phaser.Scene, ws:KeyWebSocketService) {
        this.scene = scene;
        this.subscribeWs(ws)
    }

    subscribeWs(ws:KeyWebSocketService){
        
         ws.message.subscribe(message => {
            const parsedMessage: any = JSON.parse(message);

            if(parsedMessage['type']=="kills"){
                const numberKilled = parsedMessage['data'] as number;
                this.updateScore(numberKilled);               
                console.log("killed message",this.score);
            }
        });
    }

    updateScore(points: number) {
        this.score += points;
        this?.scene.events.emit('updateScore', this.score);
    }
}