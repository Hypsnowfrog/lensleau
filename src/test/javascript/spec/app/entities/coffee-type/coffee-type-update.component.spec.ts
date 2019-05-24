/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { LensleauTestModule } from '../../../test.module';
import { CoffeeTypeUpdateComponent } from 'app/entities/coffee-type/coffee-type-update.component';
import { CoffeeTypeService } from 'app/entities/coffee-type/coffee-type.service';
import { CoffeeType } from 'app/shared/model/coffee-type.model';

describe('Component Tests', () => {
    describe('CoffeeType Management Update Component', () => {
        let comp: CoffeeTypeUpdateComponent;
        let fixture: ComponentFixture<CoffeeTypeUpdateComponent>;
        let service: CoffeeTypeService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [LensleauTestModule],
                declarations: [CoffeeTypeUpdateComponent]
            })
                .overrideTemplate(CoffeeTypeUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(CoffeeTypeUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CoffeeTypeService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new CoffeeType(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.coffeeType = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new CoffeeType();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.coffeeType = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});
