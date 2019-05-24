export interface ICountry {
    id?: number;
    name?: string;
    description?: string;
    capital?: string;
    population?: number;
    surface?: number;
    coffeeProduction?: number;
}

export class Country implements ICountry {
    constructor(
        public id?: number,
        public name?: string,
        public description?: string,
        public capital?: string,
        public population?: number,
        public surface?: number,
        public coffeeProduction?: number
    ) {}
}
