import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../log.test-samples';

import { LogFormService } from './log-form.service';

describe('Log Form Service', () => {
  let service: LogFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LogFormService);
  });

  describe('Service methods', () => {
    describe('createLogFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createLogFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            action: expect.any(Object),
            timestamp: expect.any(Object),
            ipUtilisateur: expect.any(Object),
            resultat: expect.any(Object),
            description: expect.any(Object),
            utilisateur: expect.any(Object),
          }),
        );
      });

      it('passing ILog should create a new form with FormGroup', () => {
        const formGroup = service.createLogFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            action: expect.any(Object),
            timestamp: expect.any(Object),
            ipUtilisateur: expect.any(Object),
            resultat: expect.any(Object),
            description: expect.any(Object),
            utilisateur: expect.any(Object),
          }),
        );
      });
    });

    describe('getLog', () => {
      it('should return NewLog for default Log initial value', () => {
        const formGroup = service.createLogFormGroup(sampleWithNewData);

        const log = service.getLog(formGroup) as any;

        expect(log).toMatchObject(sampleWithNewData);
      });

      it('should return NewLog for empty Log initial value', () => {
        const formGroup = service.createLogFormGroup();

        const log = service.getLog(formGroup) as any;

        expect(log).toMatchObject({});
      });

      it('should return ILog', () => {
        const formGroup = service.createLogFormGroup(sampleWithRequiredData);

        const log = service.getLog(formGroup) as any;

        expect(log).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ILog should not enable id FormControl', () => {
        const formGroup = service.createLogFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewLog should disable id FormControl', () => {
        const formGroup = service.createLogFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
