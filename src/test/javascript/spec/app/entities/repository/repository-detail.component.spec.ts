/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { ToppackTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { RepositoryDetailComponent } from '../../../../../../main/webapp/app/entities/repository/repository-detail.component';
import { RepositoryService } from '../../../../../../main/webapp/app/entities/repository/repository.service';
import { Repository } from '../../../../../../main/webapp/app/entities/repository/repository.model';

describe('Component Tests', () => {

    describe('Repository Management Detail Component', () => {
        let comp: RepositoryDetailComponent;
        let fixture: ComponentFixture<RepositoryDetailComponent>;
        let service: RepositoryService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [ToppackTestModule],
                declarations: [RepositoryDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    RepositoryService,
                    JhiEventManager
                ]
            }).overrideTemplate(RepositoryDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(RepositoryDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RepositoryService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Repository(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.repository).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
