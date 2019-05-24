import { IUser } from 'app/core/user/user.model';
import { ICoffeeType } from 'app/shared/model/coffee-type.model';

export interface IStock {
    id?: number;
    quantity?: number;
    jhi_user?: IUser;
    coffee_type?: ICoffeeType;
}

export class Stock implements IStock {
    constructor(public id?: number, public quantity?: number, public jhi_user?: IUser, public coffee_type?: ICoffeeType) {}
}
