import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Repository } from './repository.model';
import { RepositoryService } from './repository.service';

@Component({
    selector: 'jhi-repository-detail',
    templateUrl: './repository-detail.component.html'
})
export class RepositoryDetailComponent implements OnInit, OnDestroy {

    repository: Repository;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private repositoryService: RepositoryService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInRepositories();
    }

    load(id) {
        this.repositoryService.find(id).subscribe((repository) => {
            this.repository = repository;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInRepositories() {
        this.eventSubscriber = this.eventManager.subscribe(
            'repositoryListModification',
            (response) => this.load(this.repository.id)
        );
    }
}
