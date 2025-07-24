import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';
import { UtilisateurService } from 'app/entities/utilisateur/service/utilisateur.service';
import { IAlerteSecurite } from '../alerte-securite.model';
import { AlerteSecuriteService } from '../service/alerte-securite.service';
import { AlerteSecuriteFormGroup, AlerteSecuriteFormService } from './alerte-securite-form.service';

@Component({
  selector: 'jhi-alerte-securite-update',
  templateUrl: './alerte-securite-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AlerteSecuriteUpdateComponent implements OnInit {
  isSaving = false;
  alerteSecurite: IAlerteSecurite | null = null;

  utilisateursSharedCollection: IUtilisateur[] = [];

  protected alerteSecuriteService = inject(AlerteSecuriteService);
  protected alerteSecuriteFormService = inject(AlerteSecuriteFormService);
  protected utilisateurService = inject(UtilisateurService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AlerteSecuriteFormGroup = this.alerteSecuriteFormService.createAlerteSecuriteFormGroup();

  compareUtilisateur = (o1: IUtilisateur | null, o2: IUtilisateur | null): boolean => this.utilisateurService.compareUtilisateur(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ alerteSecurite }) => {
      this.alerteSecurite = alerteSecurite;
      if (alerteSecurite) {
        this.updateForm(alerteSecurite);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const alerteSecurite = this.alerteSecuriteFormService.getAlerteSecurite(this.editForm);
    if (alerteSecurite.id !== null) {
      this.subscribeToSaveResponse(this.alerteSecuriteService.update(alerteSecurite));
    } else {
      this.subscribeToSaveResponse(this.alerteSecuriteService.create(alerteSecurite));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAlerteSecurite>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(alerteSecurite: IAlerteSecurite): void {
    this.alerteSecurite = alerteSecurite;
    this.alerteSecuriteFormService.resetForm(this.editForm, alerteSecurite);

    this.utilisateursSharedCollection = this.utilisateurService.addUtilisateurToCollectionIfMissing<IUtilisateur>(
      this.utilisateursSharedCollection,
      alerteSecurite.utilisateur,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.utilisateurService
      .query()
      .pipe(map((res: HttpResponse<IUtilisateur[]>) => res.body ?? []))
      .pipe(
        map((utilisateurs: IUtilisateur[]) =>
          this.utilisateurService.addUtilisateurToCollectionIfMissing<IUtilisateur>(utilisateurs, this.alerteSecurite?.utilisateur),
        ),
      )
      .subscribe((utilisateurs: IUtilisateur[]) => (this.utilisateursSharedCollection = utilisateurs));
  }
}
