import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';
import { UtilisateurService } from 'app/entities/utilisateur/service/utilisateur.service';
import { AlerteSecuriteService } from '../service/alerte-securite.service';
import { IAlerteSecurite } from '../alerte-securite.model';
import { AlerteSecuriteFormService } from './alerte-securite-form.service';

import { AlerteSecuriteUpdateComponent } from './alerte-securite-update.component';

describe('AlerteSecurite Management Update Component', () => {
  let comp: AlerteSecuriteUpdateComponent;
  let fixture: ComponentFixture<AlerteSecuriteUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let alerteSecuriteFormService: AlerteSecuriteFormService;
  let alerteSecuriteService: AlerteSecuriteService;
  let utilisateurService: UtilisateurService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [AlerteSecuriteUpdateComponent],
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
      .overrideTemplate(AlerteSecuriteUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AlerteSecuriteUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    alerteSecuriteFormService = TestBed.inject(AlerteSecuriteFormService);
    alerteSecuriteService = TestBed.inject(AlerteSecuriteService);
    utilisateurService = TestBed.inject(UtilisateurService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Utilisateur query and add missing value', () => {
      const alerteSecurite: IAlerteSecurite = { id: 1738 };
      const utilisateur: IUtilisateur = { id: 2179 };
      alerteSecurite.utilisateur = utilisateur;

      const utilisateurCollection: IUtilisateur[] = [{ id: 2179 }];
      jest.spyOn(utilisateurService, 'query').mockReturnValue(of(new HttpResponse({ body: utilisateurCollection })));
      const additionalUtilisateurs = [utilisateur];
      const expectedCollection: IUtilisateur[] = [...additionalUtilisateurs, ...utilisateurCollection];
      jest.spyOn(utilisateurService, 'addUtilisateurToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ alerteSecurite });
      comp.ngOnInit();

      expect(utilisateurService.query).toHaveBeenCalled();
      expect(utilisateurService.addUtilisateurToCollectionIfMissing).toHaveBeenCalledWith(
        utilisateurCollection,
        ...additionalUtilisateurs.map(expect.objectContaining),
      );
      expect(comp.utilisateursSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const alerteSecurite: IAlerteSecurite = { id: 1738 };
      const utilisateur: IUtilisateur = { id: 2179 };
      alerteSecurite.utilisateur = utilisateur;

      activatedRoute.data = of({ alerteSecurite });
      comp.ngOnInit();

      expect(comp.utilisateursSharedCollection).toContainEqual(utilisateur);
      expect(comp.alerteSecurite).toEqual(alerteSecurite);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAlerteSecurite>>();
      const alerteSecurite = { id: 2075 };
      jest.spyOn(alerteSecuriteFormService, 'getAlerteSecurite').mockReturnValue(alerteSecurite);
      jest.spyOn(alerteSecuriteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ alerteSecurite });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: alerteSecurite }));
      saveSubject.complete();

      // THEN
      expect(alerteSecuriteFormService.getAlerteSecurite).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(alerteSecuriteService.update).toHaveBeenCalledWith(expect.objectContaining(alerteSecurite));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAlerteSecurite>>();
      const alerteSecurite = { id: 2075 };
      jest.spyOn(alerteSecuriteFormService, 'getAlerteSecurite').mockReturnValue({ id: null });
      jest.spyOn(alerteSecuriteService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ alerteSecurite: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: alerteSecurite }));
      saveSubject.complete();

      // THEN
      expect(alerteSecuriteFormService.getAlerteSecurite).toHaveBeenCalled();
      expect(alerteSecuriteService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAlerteSecurite>>();
      const alerteSecurite = { id: 2075 };
      jest.spyOn(alerteSecuriteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ alerteSecurite });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(alerteSecuriteService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareUtilisateur', () => {
      it('should forward to utilisateurService', () => {
        const entity = { id: 2179 };
        const entity2 = { id: 31928 };
        jest.spyOn(utilisateurService, 'compareUtilisateur');
        comp.compareUtilisateur(entity, entity2);
        expect(utilisateurService.compareUtilisateur).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
