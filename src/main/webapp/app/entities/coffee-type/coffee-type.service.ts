import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ICoffeeType } from 'app/shared/model/coffee-type.model';

type EntityResponseType = HttpResponse<ICoffeeType>;
type EntityArrayResponseType = HttpResponse<ICoffeeType[]>;

@Injectable({ providedIn: 'root' })
export class CoffeeTypeService {
    public resourceUrl = SERVER_API_URL + 'api/coffee-types';

    constructor(protected http: HttpClient) {}

    create(coffeeType: ICoffeeType): Observable<EntityResponseType> {
        return this.http.post<ICoffeeType>(this.resourceUrl, coffeeType, { observe: 'response' });
    }

    update(coffeeType: ICoffeeType): Observable<EntityResponseType> {
        return this.http.put<ICoffeeType>(this.resourceUrl, coffeeType, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ICoffeeType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ICoffeeType[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
