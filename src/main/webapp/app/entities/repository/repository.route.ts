import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { RepositoryComponent } from './repository.component';
import { RepositoryDetailComponent } from './repository-detail.component';
import { RepositoryPopupComponent } from './repository-dialog.component';
import { RepositoryDeletePopupComponent } from './repository-delete-dialog.component';

export const repositoryRoute: Routes = [
    {
        path: 'repository',
        component: RepositoryComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Repositories'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'repository/:id',
        component: RepositoryDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Repositories'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const repositoryPopupRoute: Routes = [
    {
        path: 'repository-new',
        component: RepositoryPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Repositories'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'repository/:id/edit',
        component: RepositoryPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Repositories'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'repository/:id/delete',
        component: RepositoryDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Repositories'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
