import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICoffeeType } from 'app/shared/model/coffee-type.model';

@Component({
    selector: 'jhi-coffee-type-detail',
    templateUrl: './coffee-type-detail.component.html'
})
export class CoffeeTypeDetailComponent implements OnInit {
    coffeeType: ICoffeeType;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ coffeeType }) => {
            this.coffeeType = coffeeType;
        });
    }

    previousState() {
        window.history.back();
    }
}
