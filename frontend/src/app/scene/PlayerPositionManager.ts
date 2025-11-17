import { KeyWebSocketService } from '../controller/webSockets/KeyWebSocketService';
import { Direction } from './DirectionEnum';

export class PlayerPositionManager{
    
    //ASK for Tiles
    private positionLastFetched = { x: 0, y: 0 };
    private visibleRadius = 30;
    private fetchTreshold = 5;

    //updatePlayer position
    private lastSendTime: number = Date.now();
    private sendInterval: number = 100;
    private lastSentPosition = { x: 0, y: 0 };

    private tileSize:number;
    private ws : KeyWebSocketService;
    private playersPosition = new Map<string,{ x: number, y: number }>()
    public playerPosition :{ x: number, y: number } = { x: 0, y: 0 };
    private playersGroup!: Phaser.GameObjects.Group;
    private scene: Phaser.Scene;
    private centerX;
    private centerY;

    constructor(tileSize:number,scene:Phaser.Scene,ws:KeyWebSocketService){
        this.tileSize = tileSize;
        this.ws = ws;
        this.scene = scene;
        this.centerX = scene.scale.width / 2;
        this.centerY = this.scene.scale.height / 2;
        this.playersGroup = this.scene.add.group();
        this.subscribeWs(ws);
    }

    subscribeWs(ws:KeyWebSocketService){
        ws.message.subscribe(message => {
            const parsedMessage: any = JSON.parse(message);

            if(parsedMessage['type']=="heroes"){
                const data = parsedMessage.data as Record<string, { x: number; y: number }>;
                for (const [key, value] of Object.entries(data)) {
                    const coord = { x: value.x, y: value.y };
                    this.playersPosition.set(key, coord);
                }
                this.movePlayers();
            }
        });
    }
    moveSpriteY(speed: number) {
        this.playersGroup.incY(speed);
    }
    moveSpriteX(speed: number) {
        this.playersGroup.incX(speed);
    }
    movePlayers() {
  if (!this.playersGroup) return;

  const children = this.playersGroup.getChildren();

  // The local player's world position
  const playerWorldX = this.playerPosition.x;
  const playerWorldY = this.playerPosition.y;

  // Screen center (or the point where your player sprite stays fixed)
  const centerX = this.scene.scale.width / 2;
  const centerY = this.scene.scale.height / 2;

  for (const [id, coord] of this.playersPosition.entries()) {
    if (id === localStorage.getItem("userId")) continue;

    const existingSprite = children.find(
      (child) => (child as Phaser.GameObjects.Sprite).getData("id") === id
    ) as Phaser.GameObjects.Sprite | undefined;


    const screenX = centerX + (coord.x * this.tileSize - playerWorldX) ;
    const screenY = centerY + (coord.y * this.tileSize- playerWorldY) ;

    if (!existingSprite) {
      const sprite = this.scene.add.sprite(screenX, screenY, "John-idle");
      
      sprite.setDepth(99);
      this.playersGroup.add(sprite);
    } else {
      existingSprite.x = screenX;
      existingSprite.y = screenY;
    }
  }
}

    updateServerWithPlayerPosition(map:Map<string,number>,playerPosition: { x: number; y: number; },direction:Direction){

        this.sendPlayerPositionIfConditionFulfilleld(playerPosition , direction);


        const distanceX = Math.floor((playerPosition.x-this.positionLastFetched.x)/ this.tileSize);
        const distanceY = Math.floor((playerPosition.y-this.positionLastFetched.y)/ this.tileSize);
        const distanceMoved = Math.sqrt(distanceX * distanceX + distanceY * distanceY);
            

        if (distanceMoved >= this.fetchTreshold) {
            const playerTileX = Math.floor(playerPosition.x / this.tileSize);
            const playerTileY = Math.floor(playerPosition.y / this.tileSize);
            this.checkAndFetchTiles(playerTileX,playerTileY,map);
            this.positionLastFetched = { x: playerPosition.x, y: playerPosition.y };            
        }
    }

    //updatePlayer position
    sendPlayerPositionIfConditionFulfilleld(playerPosition: { x: number; y: number; }, direction: Direction): void {
        const currentTime = Date.now();
        if ((currentTime - this.lastSendTime) >= this.sendInterval) {
            const deltaX = (playerPosition.x - this.lastSentPosition.x) / this.tileSize;
            const deltaY = (playerPosition.y - this.lastSentPosition.y) / this.tileSize;
            if (Math.abs(deltaX) >= 0.25 || Math.abs(deltaY) >= 0.25) {
                this.ws.sendPositionUpdate({
                    x: playerPosition.x/ this.tileSize,
                    y: playerPosition.y/ this.tileSize,
                    direction: direction
                });

                this.lastSentPosition = { ...playerPosition };
                this.lastSendTime = currentTime;
            }
        }
    }
    //ASK for Tiles
    checkAndFetchTiles(playerTileX:number,playerTileY:number,map:Map<string,number>) {
        const fetchSet = new Set<string>();
        for (let x = playerTileX - this.visibleRadius; x <= playerTileX + this.visibleRadius; x++) {
            for (let y = playerTileY - this.visibleRadius; y <= playerTileY + this.visibleRadius; y++) {
                const tileKey = `${x};${y}`;
                if (!map.has(tileKey)) {
                    fetchSet.add(tileKey);
                }
            }
        }
        this.fetchTile(fetchSet);
    }

    fetchTile(tilesTofetch:Set<string>): void {
        this.ws.fetchTiles(tilesTofetch);
    }
    
}