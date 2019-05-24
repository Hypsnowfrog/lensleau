/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LensleauTestModule } from '../../../test.module';
import { CoffeeTypeDetailComponent } from 'app/entities/coffee-type/coffee-type-detail.component';
import { CoffeeType } from 'app/shared/model/coffee-type.model';

describe('Component Tests', () => {
    describe('CoffeeType Management Detail Component', () => {
        let comp: CoffeeTypeDetailComponent;
        let fixture: ComponentFixture<CoffeeTypeDetailComponent>;
        const route = ({ data: of({ coffeeType: new CoffeeType(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [LensleauTestModule],
                declarations: [CoffeeTypeDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(CoffeeTypeDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(CoffeeTypeDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.coffeeType).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
