import Phaser from 'phaser';
import { KeyWebSocketService } from '../controller/webSockets/KeyWebSocketService';
import { FrontendSkillManager } from '../game/skills/FrontendSkillsManager';
import { Direction } from './DirectionEnum';
import { GameController } from '../controller/GameController';
import { MonsterDto } from '../MonsterDto';
import {PlayerPositionManager} from './PlayerPositionManager'
import { Monsters } from './Monsters';
import { Maps } from './Maps';


export class MainScene extends Phaser.Scene {
    
    private player!: Phaser.GameObjects.Sprite;
    
    private skills:String[] = [];
    private skillManager!: FrontendSkillManager;
    private isPlayingSkillAnimation = false;
    private direction = Direction.Down ;
    
    private ws!: KeyWebSocketService;
    private gameController:GameController;

    private ready :boolean = false;
    private playerPosition={ x: 0, y: 0 };
    private tileSize = 16;

    private playerPositionManager!:PlayerPositionManager;
    private monstersManager!: Monsters;
    private mapManager!: Maps;

    constructor(gameController:GameController) {
        super({ key: 'MainScene' });
        this.gameController = gameController;
    }

    init(data: { ws: KeyWebSocketService }): void {
        this.ws = data.ws;
        this.playerPositionManager = new PlayerPositionManager(this.tileSize,this,this.ws);
        const getPlayerPosition = (): { x: number, y: number } => {
            return { x: this.playerPositionManager.playerPosition.x, y: this.playerPositionManager.playerPosition.y };
        }
        this.monstersManager = new Monsters(this.tileSize,this,this.ws, getPlayerPosition);
        this.mapManager = new Maps(this.tileSize,this,this.ws, getPlayerPosition);
    }


    preload(): void {
        this.load.spritesheet('John-move', 'assets/John/Sword_Run_with_shadow.png', { frameWidth: 64, frameHeight: 64 });
        this.load.spritesheet('John-idle', 'assets/John/Sword_Idle_with_shadow.png', {frameWidth: 64,frameHeight: 64});

        this.load.spritesheet('objects','assets/John/Sword_attack_with_shadow.png',{frameWidth: 64,frameHeight: 64});
        this.load.spritesheet('tiles','assets/grass2.png',{frameWidth: this.tileSize,frameHeight: this.tileSize});
        this.load.image('zombie','assets/zombie.png');

        const skills = this.getSkills();
        this.loadSkillsFromBackend(skills);
        const centerX = this.scale.width / 2;
        const centerY = this.scale.height / 2;
        this.playerPositionManager.playerPosition = { x: centerX, y: centerY };    
    }
    
    create(): void {
    const listKey = ['z', 'q', 's', 'd'];
    this.input.keyboard?.on('keydown', (event: KeyboardEvent) => {
        const key = event.key.toLowerCase();
        if (!listKey.includes(key)) {
            const skill = this.playSkill(key);
            if (skill !== "") {
                this.ws.sendKeyPress(skill, this.direction);
            }
        }
    });
    const roomId = localStorage.getItem("roomId");
    if (roomId){
        this.gameController.readyForGame(roomId).subscribe({
        next: () => {
            console.log('Ready acknowledged by backend');
            this.ready = true;
        },
        error: err => console.error('Error sending ready', err)
    });

    this.createAnimations();
    this.skillManager.createAnimations();
    }
    
}

    override update(): void {
        this.handleMovement();

    }

    playSkill(key: string): string {
    return this.skillManager.playSkill(key, this.player,this.direction, (isPlaying: boolean) => {
        this.isPlayingSkillAnimation = isPlaying;
    });
    }

    loadSkillsFromBackend(skillDataList: any[]): void {
        this.skillManager = new FrontendSkillManager(this);
        this.skillManager.loadSkills(skillDataList);
        
    }
    
    moveGroupsX(speed:integer){
        this.mapManager.moveSpriteX(-speed);
        this.monstersManager.moveSpriteX(-speed);
        this.playerPositionManager.moveSpriteX(-speed)
        this.playerPositionManager.playerPosition.x += speed;
    }
    moveGroupsY(speed:integer){
        this.mapManager.moveSpriteY(-speed);
        this.monstersManager.moveSpriteY(-speed);
        this.playerPositionManager.moveSpriteY(-speed)
        this.playerPositionManager.playerPosition.y += speed;
    }
    
    handleMovement() :void {

        const cursors = {
            up: this.input.keyboard?.addKey(Phaser.Input.Keyboard.KeyCodes.Z),
            left: this.input.keyboard?.addKey(Phaser.Input.Keyboard.KeyCodes.Q),
            right: this.input.keyboard?.addKey(Phaser.Input.Keyboard.KeyCodes.D),
            down: this.input.keyboard?.addKey(Phaser.Input.Keyboard.KeyCodes.S),
            
        };

        const speed = 2;

        if(!this.getIsPlayingSkillAnimation()){
            if( cursors.left?.isDown) {
                this.moveGroupsX(-speed)
                this.player.anims.play('walk-left', true);
                this.direction = Direction.Left;
            }
            else if( cursors.right?.isDown) {
                this.moveGroupsX(speed)
                this.player.anims.play('walk-right', true);
                this.direction = Direction.Right;
            }
            else if( cursors.up?.isDown) {
                this.moveGroupsY(-speed)
                this.player.anims.play('walk-up', true);
                this.direction = Direction.Up;
            }
            else if( cursors.down?.isDown) {
                this.moveGroupsY(speed)
                this.player.anims.play('walk-down', true);
                this.direction = Direction.Down;
            }
            if (this.ready){
                this.playerPositionManager.updateServerWithPlayerPosition(this.mapManager.getMap(),this.playerPositionManager.playerPosition,this.direction);
            }
            
            
        }


    }
    createAnimations(): void {
        this.anims.create({
        key: 'idle',
        frames: this.anims.generateFrameNumbers('John-idle', { start: 0, end: 3 }),
        frameRate: 5,
        repeat: -1,
    });
        this.anims.create({
        key: 'walk-left',
        frames: this.anims.generateFrameNumbers('John-move', { start: 8, end: 15 }),
        frameRate: 10,
        repeat: -1,
    });

    this.anims.create({
        key: 'walk-right',
        frames: this.anims.generateFrameNumbers('John-move', { start: 16, end: 23 }),
        frameRate: 10,
        repeat: -1,
    });

    this.anims.create({
        key: 'walk-up',
        frames: this.anims.generateFrameNumbers('John-move', { start: 24, end: 31 }),
        frameRate: 10,
        repeat: -1,
    });

    this.anims.create({
        key: 'walk-down',
        frames: this.anims.generateFrameNumbers('John-move', { start: 0, end: 7 }),
        frameRate: 10,
        repeat: -1,
    });
    this.player = this.add.sprite(this.scale.width / 2, this.scale.height / 2, 'John-idle');
    this.player.setDepth(100);
    }

    getSkills(){
        const skills = [
            "slash"
        ]
        return skills;
    }
    getIsPlayingSkillAnimation(): boolean {
        return this.isPlayingSkillAnimation;
    }
    setIsPlayingSkillAnimation(value: boolean): void {
        this.isPlayingSkillAnimation = value;
    }
    
}