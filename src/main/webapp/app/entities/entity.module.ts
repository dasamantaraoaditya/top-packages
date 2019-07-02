import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { ToppackRepositoryModule } from './repository/repository.module';
import { ToppackPackagesModule } from './packages/packages.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        ToppackRepositoryModule,
        ToppackPackagesModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ToppackEntityModule {}
