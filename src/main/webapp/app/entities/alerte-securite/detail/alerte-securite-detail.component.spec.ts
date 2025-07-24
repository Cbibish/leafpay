import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { AlerteSecuriteDetailComponent } from './alerte-securite-detail.component';

describe('AlerteSecurite Management Detail Component', () => {
  let comp: AlerteSecuriteDetailComponent;
  let fixture: ComponentFixture<AlerteSecuriteDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AlerteSecuriteDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./alerte-securite-detail.component').then(m => m.AlerteSecuriteDetailComponent),
              resolve: { alerteSecurite: () => of({ id: 2075 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(AlerteSecuriteDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AlerteSecuriteDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load alerteSecurite on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', AlerteSecuriteDetailComponent);

      // THEN
      expect(instance.alerteSecurite()).toEqual(expect.objectContaining({ id: 2075 }));
    });
  });

  describe('PreviousState', () => {
    it('should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
