import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { ICoffeeType } from 'app/shared/model/coffee-type.model';
import { CoffeeTypeService } from './coffee-type.service';

@Component({
    selector: 'jhi-coffee-type-update',
    templateUrl: './coffee-type-update.component.html'
})
export class CoffeeTypeUpdateComponent implements OnInit {
    coffeeType: ICoffeeType;
    isSaving: boolean;

    constructor(protected coffeeTypeService: CoffeeTypeService, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ coffeeType }) => {
            this.coffeeType = coffeeType;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.coffeeType.id !== undefined) {
            this.subscribeToSaveResponse(this.coffeeTypeService.update(this.coffeeType));
        } else {
            this.subscribeToSaveResponse(this.coffeeTypeService.create(this.coffeeType));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<ICoffeeType>>) {
        result.subscribe((res: HttpResponse<ICoffeeType>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }
}
