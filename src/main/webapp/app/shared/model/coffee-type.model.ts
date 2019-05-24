export interface ICoffeeType {
    id?: number;
    coffeeType?: string;
}

export class CoffeeType implements ICoffeeType {
    constructor(public id?: number, public coffeeType?: string) {}
}
