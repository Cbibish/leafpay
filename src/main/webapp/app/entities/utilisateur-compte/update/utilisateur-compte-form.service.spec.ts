import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../utilisateur-compte.test-samples';

import { UtilisateurCompteFormService } from './utilisateur-compte-form.service';

describe('UtilisateurCompte Form Service', () => {
  let service: UtilisateurCompteFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UtilisateurCompteFormService);
  });

  describe('Service methods', () => {
    describe('createUtilisateurCompteFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createUtilisateurCompteFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            roleUtilisateurSurCeCompte: expect.any(Object),
            utilisateur: expect.any(Object),
            compte: expect.any(Object),
          }),
        );
      });

      it('passing IUtilisateurCompte should create a new form with FormGroup', () => {
        const formGroup = service.createUtilisateurCompteFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            roleUtilisateurSurCeCompte: expect.any(Object),
            utilisateur: expect.any(Object),
            compte: expect.any(Object),
          }),
        );
      });
    });

    describe('getUtilisateurCompte', () => {
      it('should return NewUtilisateurCompte for default UtilisateurCompte initial value', () => {
        const formGroup = service.createUtilisateurCompteFormGroup(sampleWithNewData);

        const utilisateurCompte = service.getUtilisateurCompte(formGroup) as any;

        expect(utilisateurCompte).toMatchObject(sampleWithNewData);
      });

      it('should return NewUtilisateurCompte for empty UtilisateurCompte initial value', () => {
        const formGroup = service.createUtilisateurCompteFormGroup();

        const utilisateurCompte = service.getUtilisateurCompte(formGroup) as any;

        expect(utilisateurCompte).toMatchObject({});
      });

      it('should return IUtilisateurCompte', () => {
        const formGroup = service.createUtilisateurCompteFormGroup(sampleWithRequiredData);

        const utilisateurCompte = service.getUtilisateurCompte(formGroup) as any;

        expect(utilisateurCompte).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IUtilisateurCompte should not enable id FormControl', () => {
        const formGroup = service.createUtilisateurCompteFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewUtilisateurCompte should disable id FormControl', () => {
        const formGroup = service.createUtilisateurCompteFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
