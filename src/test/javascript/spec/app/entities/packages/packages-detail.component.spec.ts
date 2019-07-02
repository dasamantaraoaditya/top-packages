/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { ToppackTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { PackagesDetailComponent } from '../../../../../../main/webapp/app/entities/packages/packages-detail.component';
import { PackagesService } from '../../../../../../main/webapp/app/entities/packages/packages.service';
import { Packages } from '../../../../../../main/webapp/app/entities/packages/packages.model';

describe('Component Tests', () => {

    describe('Packages Management Detail Component', () => {
        let comp: PackagesDetailComponent;
        let fixture: ComponentFixture<PackagesDetailComponent>;
        let service: PackagesService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [ToppackTestModule],
                declarations: [PackagesDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    PackagesService,
                    JhiEventManager
                ]
            }).overrideTemplate(PackagesDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(PackagesDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PackagesService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Packages(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.packages).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
