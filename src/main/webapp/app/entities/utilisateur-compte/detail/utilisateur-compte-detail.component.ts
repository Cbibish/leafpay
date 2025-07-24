import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IUtilisateurCompte } from '../utilisateur-compte.model';

@Component({
  selector: 'jhi-utilisateur-compte-detail',
  templateUrl: './utilisateur-compte-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class UtilisateurCompteDetailComponent {
  utilisateurCompte = input<IUtilisateurCompte | null>(null);

  previousState(): void {
    window.history.back();
  }
}
