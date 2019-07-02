import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ToppackSharedModule } from '../../shared';
import {
    RepositoryService,
    RepositoryPopupService,
    RepositoryComponent,
    RepositoryDetailComponent,
    RepositoryDialogComponent,
    RepositoryPopupComponent,
    RepositoryDeletePopupComponent,
    RepositoryDeleteDialogComponent,
    repositoryRoute,
    repositoryPopupRoute,
} from './';

const ENTITY_STATES = [
    ...repositoryRoute,
    ...repositoryPopupRoute,
];

@NgModule({
    imports: [
        ToppackSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        RepositoryComponent,
        RepositoryDetailComponent,
        RepositoryDialogComponent,
        RepositoryDeleteDialogComponent,
        RepositoryPopupComponent,
        RepositoryDeletePopupComponent,
    ],
    entryComponents: [
        RepositoryComponent,
        RepositoryDialogComponent,
        RepositoryPopupComponent,
        RepositoryDeleteDialogComponent,
        RepositoryDeletePopupComponent,
    ],
    providers: [
        RepositoryService,
        RepositoryPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ToppackRepositoryModule {}
