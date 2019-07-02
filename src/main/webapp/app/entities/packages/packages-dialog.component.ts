import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Packages } from './packages.model';
import { PackagesPopupService } from './packages-popup.service';
import { PackagesService } from './packages.service';
import { Repository, RepositoryService } from '../repository';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-packages-dialog',
    templateUrl: './packages-dialog.component.html'
})
export class PackagesDialogComponent implements OnInit {

    packages: Packages;
    isSaving: boolean;

    repositories: Repository[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private packagesService: PackagesService,
        private repositoryService: RepositoryService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.repositoryService.query()
            .subscribe((res: ResponseWrapper) => { this.repositories = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.packages.id !== undefined) {
            this.subscribeToSaveResponse(
                this.packagesService.update(this.packages));
        } else {
            this.subscribeToSaveResponse(
                this.packagesService.create(this.packages));
        }
    }

    private subscribeToSaveResponse(result: Observable<Packages>) {
        result.subscribe((res: Packages) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Packages) {
        this.eventManager.broadcast({ name: 'packagesListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackRepositoryById(index: number, item: Repository) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-packages-popup',
    template: ''
})
export class PackagesPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private packagesPopupService: PackagesPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.packagesPopupService
                    .open(PackagesDialogComponent as Component, params['id']);
            } else {
                this.packagesPopupService
                    .open(PackagesDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
