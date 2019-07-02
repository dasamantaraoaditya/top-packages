import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Repository } from './repository.model';
import { RepositoryPopupService } from './repository-popup.service';
import { RepositoryService } from './repository.service';

@Component({
    selector: 'jhi-repository-delete-dialog',
    templateUrl: './repository-delete-dialog.component.html'
})
export class RepositoryDeleteDialogComponent {

    repository: Repository;

    constructor(
        private repositoryService: RepositoryService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.repositoryService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'repositoryListModification',
                content: 'Deleted an repository'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-repository-delete-popup',
    template: ''
})
export class RepositoryDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private repositoryPopupService: RepositoryPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.repositoryPopupService
                .open(RepositoryDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
