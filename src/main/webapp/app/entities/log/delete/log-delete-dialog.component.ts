import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ILog } from '../log.model';
import { LogService } from '../service/log.service';

@Component({
  templateUrl: './log-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class LogDeleteDialogComponent {
  log?: ILog;

  protected logService = inject(LogService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.logService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
