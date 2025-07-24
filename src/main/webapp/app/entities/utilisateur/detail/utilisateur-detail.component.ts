import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IUtilisateur } from '../utilisateur.model';

@Component({
  selector: 'jhi-utilisateur-detail',
  templateUrl: './utilisateur-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class UtilisateurDetailComponent {
  utilisateur = input<IUtilisateur | null>(null);

  previousState(): void {
    window.history.back();
  }
}
