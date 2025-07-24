import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';
import { UtilisateurService } from 'app/entities/utilisateur/service/utilisateur.service';
import { ICompte } from 'app/entities/compte/compte.model';
import { CompteService } from 'app/entities/compte/service/compte.service';
import { IUtilisateurCompte } from '../utilisateur-compte.model';
import { UtilisateurCompteService } from '../service/utilisateur-compte.service';
import { UtilisateurCompteFormService } from './utilisateur-compte-form.service';

import { UtilisateurCompteUpdateComponent } from './utilisateur-compte-update.component';

describe('UtilisateurCompte Management Update Component', () => {
  let comp: UtilisateurCompteUpdateComponent;
  let fixture: ComponentFixture<UtilisateurCompteUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let utilisateurCompteFormService: UtilisateurCompteFormService;
  let utilisateurCompteService: UtilisateurCompteService;
  let utilisateurService: UtilisateurService;
  let compteService: CompteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [UtilisateurCompteUpdateComponent],
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
      .overrideTemplate(UtilisateurCompteUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(UtilisateurCompteUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    utilisateurCompteFormService = TestBed.inject(UtilisateurCompteFormService);
    utilisateurCompteService = TestBed.inject(UtilisateurCompteService);
    utilisateurService = TestBed.inject(UtilisateurService);
    compteService = TestBed.inject(CompteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Utilisateur query and add missing value', () => {
      const utilisateurCompte: IUtilisateurCompte = { id: 30926 };
      const utilisateur: IUtilisateur = { id: 2179 };
      utilisateurCompte.utilisateur = utilisateur;

      const utilisateurCollection: IUtilisateur[] = [{ id: 2179 }];
      jest.spyOn(utilisateurService, 'query').mockReturnValue(of(new HttpResponse({ body: utilisateurCollection })));
      const additionalUtilisateurs = [utilisateur];
      const expectedCollection: IUtilisateur[] = [...additionalUtilisateurs, ...utilisateurCollection];
      jest.spyOn(utilisateurService, 'addUtilisateurToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ utilisateurCompte });
      comp.ngOnInit();

      expect(utilisateurService.query).toHaveBeenCalled();
      expect(utilisateurService.addUtilisateurToCollectionIfMissing).toHaveBeenCalledWith(
        utilisateurCollection,
        ...additionalUtilisateurs.map(expect.objectContaining),
      );
      expect(comp.utilisateursSharedCollection).toEqual(expectedCollection);
    });

    it('should call Compte query and add missing value', () => {
      const utilisateurCompte: IUtilisateurCompte = { id: 30926 };
      const compte: ICompte = { id: 21096 };
      utilisateurCompte.compte = compte;

      const compteCollection: ICompte[] = [{ id: 21096 }];
      jest.spyOn(compteService, 'query').mockReturnValue(of(new HttpResponse({ body: compteCollection })));
      const additionalComptes = [compte];
      const expectedCollection: ICompte[] = [...additionalComptes, ...compteCollection];
      jest.spyOn(compteService, 'addCompteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ utilisateurCompte });
      comp.ngOnInit();

      expect(compteService.query).toHaveBeenCalled();
      expect(compteService.addCompteToCollectionIfMissing).toHaveBeenCalledWith(
        compteCollection,
        ...additionalComptes.map(expect.objectContaining),
      );
      expect(comp.comptesSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const utilisateurCompte: IUtilisateurCompte = { id: 30926 };
      const utilisateur: IUtilisateur = { id: 2179 };
      utilisateurCompte.utilisateur = utilisateur;
      const compte: ICompte = { id: 21096 };
      utilisateurCompte.compte = compte;

      activatedRoute.data = of({ utilisateurCompte });
      comp.ngOnInit();

      expect(comp.utilisateursSharedCollection).toContainEqual(utilisateur);
      expect(comp.comptesSharedCollection).toContainEqual(compte);
      expect(comp.utilisateurCompte).toEqual(utilisateurCompte);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUtilisateurCompte>>();
      const utilisateurCompte = { id: 7671 };
      jest.spyOn(utilisateurCompteFormService, 'getUtilisateurCompte').mockReturnValue(utilisateurCompte);
      jest.spyOn(utilisateurCompteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ utilisateurCompte });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: utilisateurCompte }));
      saveSubject.complete();

      // THEN
      expect(utilisateurCompteFormService.getUtilisateurCompte).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(utilisateurCompteService.update).toHaveBeenCalledWith(expect.objectContaining(utilisateurCompte));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUtilisateurCompte>>();
      const utilisateurCompte = { id: 7671 };
      jest.spyOn(utilisateurCompteFormService, 'getUtilisateurCompte').mockReturnValue({ id: null });
      jest.spyOn(utilisateurCompteService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ utilisateurCompte: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: utilisateurCompte }));
      saveSubject.complete();

      // THEN
      expect(utilisateurCompteFormService.getUtilisateurCompte).toHaveBeenCalled();
      expect(utilisateurCompteService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUtilisateurCompte>>();
      const utilisateurCompte = { id: 7671 };
      jest.spyOn(utilisateurCompteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ utilisateurCompte });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(utilisateurCompteService.update).toHaveBeenCalled();
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

    describe('compareCompte', () => {
      it('should forward to compteService', () => {
        const entity = { id: 21096 };
        const entity2 = { id: 28274 };
        jest.spyOn(compteService, 'compareCompte');
        comp.compareCompte(entity, entity2);
        expect(compteService.compareCompte).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
