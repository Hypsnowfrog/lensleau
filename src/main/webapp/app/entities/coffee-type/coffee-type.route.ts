import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { CoffeeType } from 'app/shared/model/coffee-type.model';
import { CoffeeTypeService } from './coffee-type.service';
import { CoffeeTypeComponent } from './coffee-type.component';
import { CoffeeTypeDetailComponent } from './coffee-type-detail.component';
import { CoffeeTypeUpdateComponent } from './coffee-type-update.component';
import { CoffeeTypeDeletePopupComponent } from './coffee-type-delete-dialog.component';
import { ICoffeeType } from 'app/shared/model/coffee-type.model';

@Injectable({ providedIn: 'root' })
export class CoffeeTypeResolve implements Resolve<ICoffeeType> {
    constructor(private service: CoffeeTypeService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ICoffeeType> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<CoffeeType>) => response.ok),
                map((coffeeType: HttpResponse<CoffeeType>) => coffeeType.body)
            );
        }
        return of(new CoffeeType());
    }
}

export const coffeeTypeRoute: Routes = [
    {
        path: '',
        component: CoffeeTypeComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'lensleauApp.coffeeType.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: CoffeeTypeDetailComponent,
        resolve: {
            coffeeType: CoffeeTypeResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'lensleauApp.coffeeType.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: CoffeeTypeUpdateComponent,
        resolve: {
            coffeeType: CoffeeTypeResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'lensleauApp.coffeeType.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: CoffeeTypeUpdateComponent,
        resolve: {
            coffeeType: CoffeeTypeResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'lensleauApp.coffeeType.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const coffeeTypePopupRoute: Routes = [
    {
        path: ':id/delete',
        component: CoffeeTypeDeletePopupComponent,
        resolve: {
            coffeeType: CoffeeTypeResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'lensleauApp.coffeeType.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
