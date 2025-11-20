import { KeyWebSocketService } from "../controller/webSockets/KeyWebSocketService";
import { HeadUpDisplay } from "./HeadUpDisplay";

export class HeadUpDisplayScene extends Phaser.Scene {
    private score: number;
    private scoreBoard : Array<{ name: string; score: number }> = [];
    private scoreBoardTexts: Phaser.GameObjects.Text[] = [];
    private hud !: HeadUpDisplay;
    constructor() {
        super({ key: 'hud', active: true });
        this.score = 0;

        
    }
    init( data: { ws: KeyWebSocketService }): void{
        this.hud = new HeadUpDisplay(this, data.ws);
        
    }    

    create() {
        let info = this.add.text(10, 10, 'Your score : 0', { font: '24px Arial', color: '#000000' });
       

        this.events.on('updateScore', (score:number) => {
            this.score  = score;
            console.log("Score updated:", this.score);
            info.setText('Your Score: ' + this.score);
        });

        this.events.on('updateScoreBoard', (data: Array<{ name: string; score: number }>) => {
            this.scoreBoard = data;
            this.renderScoreBoard(data.sort((a, b) => b.score - a.score));
            console.log(" scoreboard received")
        });

    }
    private renderScoreBoard(data: Array<{ name: string; score: number }>) {

            this.scoreBoardTexts.forEach(t => t.destroy());
            this.scoreBoardTexts.length = 0;
    
            const startX = 10;
            const startY = 30;
            const rowHeight = 16;
            const headerGap = 10;

            const header = this.add.text(startX, startY, 'Leaderboard', { font: '24px Arial', color: '#000' });
            this.scoreBoardTexts.push(header);
            //console.log(data.length)
            for (let i = 0; i < data.length; i++) {
                const y = startY + headerGap + (i + 1) * rowHeight;
                const text = this.add.text(startX, y, `${i + 1}. ${data[i].name} — ${data[i].score}`, { font: '12px Arial', color: '#000' });
                this.scoreBoardTexts.push(text);

            }
            
        }
}
