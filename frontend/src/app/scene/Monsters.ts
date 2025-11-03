import { KeyWebSocketService } from "../controller/webSockets/KeyWebSocketService";
import { MonsterDto } from "../MonsterDto";
import { PlayerPositionManager } from "./PlayerPositionManager";

export class Monsters{
    private monsters = new Map<number,MonsterDto>();
    private monsterGroup!: Phaser.GameObjects.Group;
    private tileSize: number;
    private centerX: number;
    private centerY : number;
    private scene: Phaser.Scene;
    

    constructor(tileSize:number,scene:Phaser.Scene, ws:KeyWebSocketService , getPlayerPosition: () => { x: number, y: number }){
        this.tileSize = tileSize;
        this.centerX = scene.scale.width / 2;
        this.centerY = scene.scale.height / 2;
        this.scene = scene;
        this.monsterGroup = this.scene.add.group();
        this.suscribeWs(ws,getPlayerPosition);

    }
    suscribeWs(ws:KeyWebSocketService, getPlayerPosition: () => { x: number, y: number }){
        ws.message.subscribe(message => {
            const parsedMessage: any = JSON.parse(message);
            if  (parsedMessage['type'] == "monsters"){
                const monsterData = parsedMessage['data'] as Record<string, { coord: string; monsterName: string; status:string }>;
                const position = getPlayerPosition()
                this.updateMonstersWithBackendData(position,monsterData);
            }
            else if  (parsedMessage['type'] == "monstersMove"){
                const monsterData = parsedMessage['data'] as Record< string , { x: number, y:number}>;
                this.moveMonsters(monsterData);
            }
        });
    }

    moveMonsters(monsterData : Record< string , { x: number, y:number}>){
        console.log(monsterData)
        const children = this.monsterGroup.getChildren();
        for (let i = 0; i < children.length; i++) {
            const sprite = children[i] as Phaser.GameObjects.Sprite;
            const id = sprite.getData('id')
            if ( id in monsterData) {
                const monster = this.monsters.get(id);
                const newX = monsterData[id].x +sprite.x;
                const newY = monsterData[id].y +sprite.y;
                const newCoord : string=""+ newX +";"+ newY;
                if (monster){
                    monster.coord = newCoord;
                    sprite.x = monsterData[id].x * this.tileSize +sprite.x;
                    sprite.y = monsterData[id].y * this.tileSize +sprite.y;
                }
            }
        }
    }

    moveMonster(key:number, x:number, y:number){
        const newCoord : string=""+x+";"+y;
        const monster = this.monsters.get(key);
        if (monster){
            const oldCoord = monster.coord.split(';');
            const oldX = parseInt(oldCoord[0], 10);
            const oldY = parseInt(oldCoord[1], 10);
            this.moveSpriteX(x-oldX);
            monster.coord = newCoord
            
        }
       
    }
    
    moveSpriteX(speed:number){
        this.monsterGroup.incX(speed);
    }
    moveSpriteY(speed:number){
        this.monsterGroup.incY(speed);
    }

    deleteMonster(id:number){
        const children = this.monsterGroup.getChildren();
        for (let i = 0; i < children.length; i++) {
            const sprite = children[i] as Phaser.GameObjects.Sprite;
            if (sprite.getData('id') === id) {
                sprite.destroy();
                this.monsters.delete(id)
            }
        }
    }

    createMonsters(playerPosition: { x: number; y: number; }, monsters:Map<number, MonsterDto>):void {
        monsters.forEach((monsterDto, id) => {
            if (!this.monsters.has(id)){
                const coordStr = monsterDto.coord;
                const coords = coordStr.split(';');
                const x = parseInt(coords[0], 10);
                const y = parseInt(coords[1], 10);

                const posX = x * this.tileSize-playerPosition.x +this.centerX;
                const posY = y * this.tileSize-playerPosition.y + this.centerY;
                const monster = this.scene.add.sprite(posX, posY, 'zombie');
                monster.setData('id',id);
                monster.setDisplaySize(this.tileSize*2, this.tileSize*2);
                this.monsterGroup.add(monster);
                this.monsters.set(id,monsterDto);
            }
            else {
                console.log(this.monsters.get(id));
            }
            

        });   
    }
    updateMonstersWithBackendData(playerPosition: { x: number; y: number; },monsterData:Record<string, { coord: string; monsterName: string; status:string }> ){
        const temporaryMonsters = new Map<integer,MonsterDto>();
        for (const [key, value] of Object.entries(monsterData)) {
            const id = Number(key);
            if (value.status =='Alive'){           
                const coordStr = value.coord;
                const name = value.monsterName;
                const monsterDto = new MonsterDto(id, coordStr, name);
                temporaryMonsters.set(id, monsterDto);
            }
            else if (value.status =='Dead'){
                const children = this.monsterGroup.getChildren();
                for (let i = 0; i < children.length; i++) {
                    const sprite = children[i] as Phaser.GameObjects.Sprite;
                    if (sprite.getData('id') === id) {
                        sprite.destroy();
                        this.monsters.delete(id)
                    }
                }
            }                 
        }
        this.createMonsters(playerPosition,temporaryMonsters);
    }
}