/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { LensleauTestModule } from '../../../test.module';
import { CoffeeTypeDeleteDialogComponent } from 'app/entities/coffee-type/coffee-type-delete-dialog.component';
import { CoffeeTypeService } from 'app/entities/coffee-type/coffee-type.service';

describe('Component Tests', () => {
    describe('CoffeeType Management Delete Component', () => {
        let comp: CoffeeTypeDeleteDialogComponent;
        let fixture: ComponentFixture<CoffeeTypeDeleteDialogComponent>;
        let service: CoffeeTypeService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [LensleauTestModule],
                declarations: [CoffeeTypeDeleteDialogComponent]
            })
                .overrideTemplate(CoffeeTypeDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(CoffeeTypeDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CoffeeTypeService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
