import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { Packages } from './packages.model';
import { PackagesService } from './packages.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-packages',
    templateUrl: './packages.component.html'
})
export class PackagesComponent implements OnInit, OnDestroy {
packages: Packages[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private packagesService: PackagesService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {
    }

    loadAll() {
        this.packagesService.query().subscribe(
            (res: ResponseWrapper) => {
                this.packages = res.json;
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInPackages();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: Packages) {
        return item.id;
    }
    registerChangeInPackages() {
        this.eventSubscriber = this.eventManager.subscribe('packagesListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
