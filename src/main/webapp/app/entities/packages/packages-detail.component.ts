import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Packages } from './packages.model';
import { PackagesService } from './packages.service';

@Component({
    selector: 'jhi-packages-detail',
    templateUrl: './packages-detail.component.html'
})
export class PackagesDetailComponent implements OnInit, OnDestroy {

    packages: Packages;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private packagesService: PackagesService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInPackages();
    }

    load(id) {
        this.packagesService.find(id).subscribe((packages) => {
            this.packages = packages;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInPackages() {
        this.eventSubscriber = this.eventManager.subscribe(
            'packagesListModification',
            (response) => this.load(this.packages.id)
        );
    }
}
