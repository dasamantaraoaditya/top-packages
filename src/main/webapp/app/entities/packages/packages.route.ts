import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { PackagesComponent } from './packages.component';
import { PackagesDetailComponent } from './packages-detail.component';
import { PackagesPopupComponent } from './packages-dialog.component';
import { PackagesDeletePopupComponent } from './packages-delete-dialog.component';

export const packagesRoute: Routes = [
    {
        path: 'packages',
        component: PackagesComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Packages'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'packages/:id',
        component: PackagesDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Packages'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const packagesPopupRoute: Routes = [
    {
        path: 'packages-new',
        component: PackagesPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Packages'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'packages/:id/edit',
        component: PackagesPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Packages'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'packages/:id/delete',
        component: PackagesDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Packages'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
