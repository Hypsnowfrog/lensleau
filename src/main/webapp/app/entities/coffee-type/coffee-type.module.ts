import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { LensleauSharedModule } from 'app/shared';
import {
    CoffeeTypeComponent,
    CoffeeTypeDetailComponent,
    CoffeeTypeUpdateComponent,
    CoffeeTypeDeletePopupComponent,
    CoffeeTypeDeleteDialogComponent,
    coffeeTypeRoute,
    coffeeTypePopupRoute
} from './';

const ENTITY_STATES = [...coffeeTypeRoute, ...coffeeTypePopupRoute];

@NgModule({
    imports: [LensleauSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        CoffeeTypeComponent,
        CoffeeTypeDetailComponent,
        CoffeeTypeUpdateComponent,
        CoffeeTypeDeleteDialogComponent,
        CoffeeTypeDeletePopupComponent
    ],
    entryComponents: [CoffeeTypeComponent, CoffeeTypeUpdateComponent, CoffeeTypeDeleteDialogComponent, CoffeeTypeDeletePopupComponent],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class LensleauCoffeeTypeModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
