import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IUtilisateurCompte } from '../utilisateur-compte.model';
import { UtilisateurCompteService } from '../service/utilisateur-compte.service';

@Component({
  templateUrl: './utilisateur-compte-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class UtilisateurCompteDeleteDialogComponent {
  utilisateurCompte?: IUtilisateurCompte;

  protected utilisateurCompteService = inject(UtilisateurCompteService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.utilisateurCompteService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
