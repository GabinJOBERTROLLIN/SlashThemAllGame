import { KeyWebSocketService } from "../controller/webSockets/KeyWebSocketService";

export class Maps{
    private map = new Map<string,number>();
    private tilesGroup!: Phaser.GameObjects.Group;

    private tileSize: number;
    private centerX: number;
    private centerY : number;
    private scene: Phaser.Scene;

    constructor(tileSize:number,scene:Phaser.Scene,ws:KeyWebSocketService, getPlayerPosition: () => { x: number, y: number }){
        this.tileSize = tileSize;
        this.centerX = scene.scale.width / 2;
        this.centerY = scene.scale.height / 2;
        this.scene = scene;
        this.tilesGroup = this.scene.add.group();
        this.subscribeWs(ws,getPlayerPosition)
    }
    subscribeWs(ws:KeyWebSocketService, getPlayerPosition: () => { x: number, y: number }){
        ws.message.subscribe(message => {
            const parsedMessage: any = JSON.parse(message);

            if(parsedMessage['type']=="map"){
                const tiles = parsedMessage['data'] as Record<string,number>;
                const position = getPlayerPosition()
                this.updateMapWithBackendData(position,tiles);
            }
        });
    }
    moveSpriteX(speed:number){
        this.tilesGroup.incX(speed);
    }
    moveSpriteY(speed:number){
        this.tilesGroup.incY(speed);
    }
    addTile(playerPosition: { x: number; y: number; },temporaryMap:Map<string, number>): void {
        temporaryMap.forEach((value, key) => {
            this.map.set(key, value); 
            const coords = key.split(';');
            const x = parseInt(coords[0], 10);
            const y = parseInt(coords[1], 10);


            const posX = x * this.tileSize - playerPosition.x+ this.centerX;
            const posY = y * this.tileSize - playerPosition.y+ this.centerY;
            const tile = this.scene.add.sprite(posX, posY, 'tiles');
            
            tile.setFrame(value);
            this.tilesGroup.add(tile);
        });
        this.tilesGroup.setDepth(-100);
    }
    updateMapWithBackendData(playerPosition: { x: number; y: number; },tiles:Record<string,number>){
        const temporaryMap = new Map<string,number>();
        for (const [key, value] of Object.entries(tiles)) {
                temporaryMap.set(key,value as number);
        }
        this.addTile(playerPosition,temporaryMap)
    }
    getMap():Map<string,number>{
        return this.map;
    }
}