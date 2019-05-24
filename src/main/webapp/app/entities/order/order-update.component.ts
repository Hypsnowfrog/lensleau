import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';
import { IOrder } from 'app/shared/model/order.model';
import { OrderService } from './order.service';
import { IUser, UserService } from 'app/core';
import { ICoffeeType } from 'app/shared/model/coffee-type.model';
import { CoffeeTypeService } from 'app/entities/coffee-type';

@Component({
    selector: 'jhi-order-update',
    templateUrl: './order-update.component.html'
})
export class OrderUpdateComponent implements OnInit {
    order: IOrder;
    isSaving: boolean;

    users: IUser[];

    coffeetypes: ICoffeeType[];
    date: string;

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected orderService: OrderService,
        protected userService: UserService,
        protected coffeeTypeService: CoffeeTypeService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ order }) => {
            this.order = order;
            this.date = this.order.date != null ? this.order.date.format(DATE_TIME_FORMAT) : null;
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
        this.order.date = this.date != null ? moment(this.date, DATE_TIME_FORMAT) : null;
        if (this.order.id !== undefined) {
            this.subscribeToSaveResponse(this.orderService.update(this.order));
        } else {
            this.subscribeToSaveResponse(this.orderService.create(this.order));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IOrder>>) {
        result.subscribe((res: HttpResponse<IOrder>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
