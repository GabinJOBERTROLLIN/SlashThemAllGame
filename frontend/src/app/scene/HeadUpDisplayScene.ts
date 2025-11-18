import { KeyWebSocketService } from "../controller/webSockets/KeyWebSocketService";
import { HeadUpDisplay } from "./HeadUpDisplay";

export class HeadUpDisplayScene extends Phaser.Scene {
    private score: number;
    private hud !: HeadUpDisplay;
    constructor() {
        super({ key: 'hud', active: true });
        this.score = 0;

        
    }
    init( data: { ws: KeyWebSocketService }): void{
        this.hud = new HeadUpDisplay(this, data.ws);
    }    

    create() {
        let info = this.add.text(10, 10, 'Score: 0', { font: '48px Arial', color: '#000000' });
       

        this.events.on('updateScore', (score:number) => {
            this.score  = score;
            console.log("Score updated:", this.score);
            info.setText('Score: ' + this.score);
        });
    }
}
