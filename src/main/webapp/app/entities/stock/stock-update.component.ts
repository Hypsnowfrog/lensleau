import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IStock } from 'app/shared/model/stock.model';
import { StockService } from './stock.service';
import { IUser, UserService } from 'app/core';
import { ICoffeeType } from 'app/shared/model/coffee-type.model';
import { CoffeeTypeService } from 'app/entities/coffee-type';

@Component({
    selector: 'jhi-stock-update',
    templateUrl: './stock-update.component.html'
})
export class StockUpdateComponent implements OnInit {
    stock: IStock;
    isSaving: boolean;

    users: IUser[];

    coffeetypes: ICoffeeType[];

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected stockService: StockService,
        protected userService: UserService,
        protected coffeeTypeService: CoffeeTypeService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ stock }) => {
            this.stock = stock;
        });
        this.userService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IUser[]>) => mayBeOk.ok),
                map((response: HttpResponse<IUser[]>) => response.body)
            )
            .subscribe((res: IUser[]) => (this.users = res), (res: HttpErrorResponse) => this.onError(res.message));
        this.coffeeTypeService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<ICoffeeType[]>) => mayBeOk.ok),
                map((response: HttpResponse<ICoffeeType[]>) => response.body)
            )
            .subscribe((res: ICoffeeType[]) => (this.coffeetypes = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.stock.id !== undefined) {
            this.subscribeToSaveResponse(this.stockService.update(this.stock));
        } else {
            this.subscribeToSaveResponse(this.stockService.create(this.stock));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IStock>>) {
        result.subscribe((res: HttpResponse<IStock>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackUserById(index: number, item: IUser) {
        return item.id;
    }

    trackCoffeeTypeById(index: number, item: ICoffeeType) {
        return item.id;
    }
}
