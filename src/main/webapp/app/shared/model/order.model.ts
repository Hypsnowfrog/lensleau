import { Moment } from 'moment';
import { IUser } from 'app/core/user/user.model';
import { ICoffeeType } from 'app/shared/model/coffee-type.model';

export interface IOrder {
    id?: number;
    quantity?: number;
    date?: Moment;
    exporter?: IUser;
    importer?: IUser;
    coffee_type?: ICoffeeType;
}

export class Order implements IOrder {
    constructor(
        public id?: number,
        public quantity?: number,
        public date?: Moment,
        public exporter?: IUser,
        public importer?: IUser,
        public coffee_type?: ICoffeeType
    ) {}
}
