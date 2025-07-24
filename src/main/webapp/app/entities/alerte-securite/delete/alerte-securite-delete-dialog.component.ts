import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IAlerteSecurite } from '../alerte-securite.model';
import { AlerteSecuriteService } from '../service/alerte-securite.service';

@Component({
  templateUrl: './alerte-securite-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class AlerteSecuriteDeleteDialogComponent {
  alerteSecurite?: IAlerteSecurite;

  protected alerteSecuriteService = inject(AlerteSecuriteService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.alerteSecuriteService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
