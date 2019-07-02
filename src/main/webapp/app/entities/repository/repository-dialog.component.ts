import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Repository } from './repository.model';
import { RepositoryPopupService } from './repository-popup.service';
import { RepositoryService } from './repository.service';

@Component({
    selector: 'jhi-repository-dialog',
    templateUrl: './repository-dialog.component.html'
})
export class RepositoryDialogComponent implements OnInit {

    repository: Repository;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private repositoryService: RepositoryService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.repository.id !== undefined) {
            this.subscribeToSaveResponse(
                this.repositoryService.update(this.repository));
        } else {
            this.subscribeToSaveResponse(
                this.repositoryService.create(this.repository));
        }
    }

    private subscribeToSaveResponse(result: Observable<Repository>) {
        result.subscribe((res: Repository) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Repository) {
        this.eventManager.broadcast({ name: 'repositoryListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }
}

@Component({
    selector: 'jhi-repository-popup',
    template: ''
})
export class RepositoryPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private repositoryPopupService: RepositoryPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.repositoryPopupService
                    .open(RepositoryDialogComponent as Component, params['id']);
            } else {
                this.repositoryPopupService
                    .open(RepositoryDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
