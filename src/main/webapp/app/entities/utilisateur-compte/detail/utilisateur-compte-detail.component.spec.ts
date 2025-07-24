import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { UtilisateurCompteDetailComponent } from './utilisateur-compte-detail.component';

describe('UtilisateurCompte Management Detail Component', () => {
  let comp: UtilisateurCompteDetailComponent;
  let fixture: ComponentFixture<UtilisateurCompteDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UtilisateurCompteDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./utilisateur-compte-detail.component').then(m => m.UtilisateurCompteDetailComponent),
              resolve: { utilisateurCompte: () => of({ id: 7671 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(UtilisateurCompteDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UtilisateurCompteDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load utilisateurCompte on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', UtilisateurCompteDetailComponent);

      // THEN
      expect(instance.utilisateurCompte()).toEqual(expect.objectContaining({ id: 7671 }));
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
