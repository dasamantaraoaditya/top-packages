import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { Repository } from './repository.model';
import { RepositoryService } from './repository.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-repository',
    templateUrl: './repository.component.html'
})
export class RepositoryComponent implements OnInit, OnDestroy {
repositories: Repository[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private repositoryService: RepositoryService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {
    }

    loadAll() {
        this.repositoryService.query().subscribe(
            (res: ResponseWrapper) => {
                this.repositories = res.json;
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInRepositories();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: Repository) {
        return item.id;
    }
    registerChangeInRepositories() {
        this.eventSubscriber = this.eventManager.subscribe('repositoryListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
