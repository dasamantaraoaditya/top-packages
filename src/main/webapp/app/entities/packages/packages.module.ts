import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ToppackSharedModule } from '../../shared';
import {
    PackagesService,
    PackagesPopupService,
    PackagesComponent,
    PackagesDetailComponent,
    PackagesDialogComponent,
    PackagesPopupComponent,
    PackagesDeletePopupComponent,
    PackagesDeleteDialogComponent,
    packagesRoute,
    packagesPopupRoute,
} from './';

const ENTITY_STATES = [
    ...packagesRoute,
    ...packagesPopupRoute,
];

@NgModule({
    imports: [
        ToppackSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        PackagesComponent,
        PackagesDetailComponent,
        PackagesDialogComponent,
        PackagesDeleteDialogComponent,
        PackagesPopupComponent,
        PackagesDeletePopupComponent,
    ],
    entryComponents: [
        PackagesComponent,
        PackagesDialogComponent,
        PackagesPopupComponent,
        PackagesDeleteDialogComponent,
        PackagesDeletePopupComponent,
    ],
    providers: [
        PackagesService,
        PackagesPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ToppackPackagesModule {}
