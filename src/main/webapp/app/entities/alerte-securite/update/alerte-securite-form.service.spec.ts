import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../alerte-securite.test-samples';

import { AlerteSecuriteFormService } from './alerte-securite-form.service';

describe('AlerteSecurite Form Service', () => {
  let service: AlerteSecuriteFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AlerteSecuriteFormService);
  });

  describe('Service methods', () => {
    describe('createAlerteSecuriteFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAlerteSecuriteFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            typeAlerte: expect.any(Object),
            niveauSeverite: expect.any(Object),
            timestamp: expect.any(Object),
            estTraitee: expect.any(Object),
            utilisateur: expect.any(Object),
          }),
        );
      });

      it('passing IAlerteSecurite should create a new form with FormGroup', () => {
        const formGroup = service.createAlerteSecuriteFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            typeAlerte: expect.any(Object),
            niveauSeverite: expect.any(Object),
            timestamp: expect.any(Object),
            estTraitee: expect.any(Object),
            utilisateur: expect.any(Object),
          }),
        );
      });
    });

    describe('getAlerteSecurite', () => {
      it('should return NewAlerteSecurite for default AlerteSecurite initial value', () => {
        const formGroup = service.createAlerteSecuriteFormGroup(sampleWithNewData);

        const alerteSecurite = service.getAlerteSecurite(formGroup) as any;

        expect(alerteSecurite).toMatchObject(sampleWithNewData);
      });

      it('should return NewAlerteSecurite for empty AlerteSecurite initial value', () => {
        const formGroup = service.createAlerteSecuriteFormGroup();

        const alerteSecurite = service.getAlerteSecurite(formGroup) as any;

        expect(alerteSecurite).toMatchObject({});
      });

      it('should return IAlerteSecurite', () => {
        const formGroup = service.createAlerteSecuriteFormGroup(sampleWithRequiredData);

        const alerteSecurite = service.getAlerteSecurite(formGroup) as any;

        expect(alerteSecurite).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAlerteSecurite should not enable id FormControl', () => {
        const formGroup = service.createAlerteSecuriteFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAlerteSecurite should disable id FormControl', () => {
        const formGroup = service.createAlerteSecuriteFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
