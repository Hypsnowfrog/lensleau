import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICoffeeType } from 'app/shared/model/coffee-type.model';
import { CoffeeTypeService } from './coffee-type.service';

@Component({
    selector: 'jhi-coffee-type-delete-dialog',
    templateUrl: './coffee-type-delete-dialog.component.html'
})
export class CoffeeTypeDeleteDialogComponent {
    coffeeType: ICoffeeType;

    constructor(
        protected coffeeTypeService: CoffeeTypeService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.coffeeTypeService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'coffeeTypeListModification',
                content: 'Deleted an coffeeType'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-coffee-type-delete-popup',
    template: ''
})
export class CoffeeTypeDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ coffeeType }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(CoffeeTypeDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.coffeeType = coffeeType;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/coffee-type', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/coffee-type', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
