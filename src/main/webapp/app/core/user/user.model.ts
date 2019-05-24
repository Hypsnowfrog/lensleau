import {Country} from 'app/shared/model/country.model';

export enum UserType {
    IMPORTER,
    EXPORTER
}

export interface IUser {
    id?: any;
    login?: string;
    firstName?: string;
    lastName?: string;
    email?: string;
    activated?: boolean;
    langKey?: string;
    authorities?: any[];
    createdBy?: string;
    createdDate?: Date;
    lastModifiedBy?: string;
    lastModifiedDate?: Date;
    password?: string;
    company?: string;
    phone?: string;
    city?: string;
    country?: Country;
    zip?: string;
    streetNum?: string;
    streetName?: string;
    userType?: UserType;
}

export class User implements IUser {
    constructor(
        public id?: any,
        public login?: string,
        public firstName?: string,
        public lastName?: string,
        public email?: string,
        public activated?: boolean,
        public langKey?: string,
        public authorities?: any[],
        public createdBy?: string,
        public createdDate?: Date,
        public lastModifiedBy?: string,
        public lastModifiedDate?: Date,
        public password?: string,
        public company?: string,
        public phone?: string,
        public city?: string,
        public country?: Country,
        public zip?: string,
        public streetNum?: string,
        public streetName?: string,
        public userType?: UserType,
    ) {
        this.id = id ? id : null;
        this.login = login ? login : null;
        this.firstName = firstName ? firstName : null;
        this.lastName = lastName ? lastName : null;
        this.email = email ? email : null;
        this.activated = activated ? activated : false;
        this.langKey = langKey ? langKey : null;
        this.authorities = authorities ? authorities : null;
        this.createdBy = createdBy ? createdBy : null;
        this.createdDate = createdDate ? createdDate : null;
        this.lastModifiedBy = lastModifiedBy ? lastModifiedBy : null;
        this.lastModifiedDate = lastModifiedDate ? lastModifiedDate : null;
        this.password = password ? password : null;
        this.company = company ? company : null;
        this.phone = phone ? phone : null;
        this.city = city ? city : null;
        this.zip = zip ? zip : null;
        this.streetNum = streetNum ? streetNum : null;
        this.streetName = streetName ? streetName : null;
        this.userType = userType ? userType : null;
    }
}
