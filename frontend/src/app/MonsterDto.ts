export class MonsterDto {
    id: number
    coord: string
    name: string

    constructor(id: number, coord: string, name: string) {
        this.id = id;
        this.coord = coord;
        this.name = name;
    }
}