import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ICoffeeType } from 'app/shared/model/coffee-type.model';
import { AccountService } from 'app/core';
import { CoffeeTypeService } from './coffee-type.service';

@Component({
    selector: 'jhi-coffee-type',
    templateUrl: './coffee-type.component.html'
})
export class CoffeeTypeComponent implements OnInit, OnDestroy {
    coffeeTypes: ICoffeeType[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        protected coffeeTypeService: CoffeeTypeService,
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        protected accountService: AccountService
    ) {}

    loadAll() {
        this.coffeeTypeService
            .query()
            .pipe(
                filter((res: HttpResponse<ICoffeeType[]>) => res.ok),
                map((res: HttpResponse<ICoffeeType[]>) => res.body)
            )
            .subscribe(
                (res: ICoffeeType[]) => {
                    this.coffeeTypes = res;
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    ngOnInit() {
        this.loadAll();
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInCoffeeTypes();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: ICoffeeType) {
        return item.id;
    }

    registerChangeInCoffeeTypes() {
        this.eventSubscriber = this.eventManager.subscribe('coffeeTypeListModification', response => this.loadAll());
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
