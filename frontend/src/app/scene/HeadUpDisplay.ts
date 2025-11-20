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
            else if( parsedMessage['type']=="damageHero"){
                const damage = parsedMessage['data'] as number;
                this.updateScore(-damage);
            }
            else if (parsedMessage['type'] == "scoreBoard"){
                console.log("Scoreboard data",parsedMessage['data']);
                const scoreBoard = parsedMessage['data'] as Array<{ name: string; score: number }>
                this.updateScoreBoard(scoreBoard);
            }
                
            }); 
    }

    updateScoreBoard(data: Array<{ name: string; score: number }>){
        this?.scene.events.emit('updateScoreBoard', data);
    }
    updateScore(points: number) {
        this.score += points;
        this?.scene.events.emit('updateScore', this.score);
    }
}