/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { LensleauTestModule } from '../../../test.module';
import { CoffeeTypeComponent } from 'app/entities/coffee-type/coffee-type.component';
import { CoffeeTypeService } from 'app/entities/coffee-type/coffee-type.service';
import { CoffeeType } from 'app/shared/model/coffee-type.model';

describe('Component Tests', () => {
    describe('CoffeeType Management Component', () => {
        let comp: CoffeeTypeComponent;
        let fixture: ComponentFixture<CoffeeTypeComponent>;
        let service: CoffeeTypeService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [LensleauTestModule],
                declarations: [CoffeeTypeComponent],
                providers: []
            })
                .overrideTemplate(CoffeeTypeComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(CoffeeTypeComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CoffeeTypeService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new CoffeeType(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.coffeeTypes[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
