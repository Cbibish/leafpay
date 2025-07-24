import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { CompteService } from '../service/compte.service';
import { ICompte } from '../compte.model';
import { CompteFormService } from './compte-form.service';

import { CompteUpdateComponent } from './compte-update.component';

describe('Compte Management Update Component', () => {
  let comp: CompteUpdateComponent;
  let fixture: ComponentFixture<CompteUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let compteFormService: CompteFormService;
  let compteService: CompteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [CompteUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(CompteUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CompteUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    compteFormService = TestBed.inject(CompteFormService);
    compteService = TestBed.inject(CompteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const compte: ICompte = { id: 28274 };

      activatedRoute.data = of({ compte });
      comp.ngOnInit();

      expect(comp.compte).toEqual(compte);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICompte>>();
      const compte = { id: 21096 };
      jest.spyOn(compteFormService, 'getCompte').mockReturnValue(compte);
      jest.spyOn(compteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ compte });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: compte }));
      saveSubject.complete();

      // THEN
      expect(compteFormService.getCompte).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(compteService.update).toHaveBeenCalledWith(expect.objectContaining(compte));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICompte>>();
      const compte = { id: 21096 };
      jest.spyOn(compteFormService, 'getCompte').mockReturnValue({ id: null });
      jest.spyOn(compteService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ compte: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: compte }));
      saveSubject.complete();

      // THEN
      expect(compteFormService.getCompte).toHaveBeenCalled();
      expect(compteService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICompte>>();
      const compte = { id: 21096 };
      jest.spyOn(compteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ compte });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(compteService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
