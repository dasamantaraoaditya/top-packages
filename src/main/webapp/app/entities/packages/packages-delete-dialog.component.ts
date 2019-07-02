import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Packages } from './packages.model';
import { PackagesPopupService } from './packages-popup.service';
import { PackagesService } from './packages.service';

@Component({
    selector: 'jhi-packages-delete-dialog',
    templateUrl: './packages-delete-dialog.component.html'
})
export class PackagesDeleteDialogComponent {

    packages: Packages;

    constructor(
        private packagesService: PackagesService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.packagesService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'packagesListModification',
                content: 'Deleted an packages'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-packages-delete-popup',
    template: ''
})
export class PackagesDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private packagesPopupService: PackagesPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.packagesPopupService
                .open(PackagesDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
