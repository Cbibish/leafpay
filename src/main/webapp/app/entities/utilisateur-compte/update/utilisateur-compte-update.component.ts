import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';
import { UtilisateurService } from 'app/entities/utilisateur/service/utilisateur.service';
import { ICompte } from 'app/entities/compte/compte.model';
import { CompteService } from 'app/entities/compte/service/compte.service';
import { UtilisateurCompteService } from '../service/utilisateur-compte.service';
import { IUtilisateurCompte } from '../utilisateur-compte.model';
import { UtilisateurCompteFormGroup, UtilisateurCompteFormService } from './utilisateur-compte-form.service';

@Component({
  selector: 'jhi-utilisateur-compte-update',
  templateUrl: './utilisateur-compte-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class UtilisateurCompteUpdateComponent implements OnInit {
  isSaving = false;
  utilisateurCompte: IUtilisateurCompte | null = null;

  utilisateursSharedCollection: IUtilisateur[] = [];
  comptesSharedCollection: ICompte[] = [];

  protected utilisateurCompteService = inject(UtilisateurCompteService);
  protected utilisateurCompteFormService = inject(UtilisateurCompteFormService);
  protected utilisateurService = inject(UtilisateurService);
  protected compteService = inject(CompteService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: UtilisateurCompteFormGroup = this.utilisateurCompteFormService.createUtilisateurCompteFormGroup();

  compareUtilisateur = (o1: IUtilisateur | null, o2: IUtilisateur | null): boolean => this.utilisateurService.compareUtilisateur(o1, o2);

  compareCompte = (o1: ICompte | null, o2: ICompte | null): boolean => this.compteService.compareCompte(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ utilisateurCompte }) => {
      this.utilisateurCompte = utilisateurCompte;
      if (utilisateurCompte) {
        this.updateForm(utilisateurCompte);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const utilisateurCompte = this.utilisateurCompteFormService.getUtilisateurCompte(this.editForm);
    if (utilisateurCompte.id !== null) {
      this.subscribeToSaveResponse(this.utilisateurCompteService.update(utilisateurCompte));
    } else {
      this.subscribeToSaveResponse(this.utilisateurCompteService.create(utilisateurCompte));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUtilisateurCompte>>): void {
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

  protected updateForm(utilisateurCompte: IUtilisateurCompte): void {
    this.utilisateurCompte = utilisateurCompte;
    this.utilisateurCompteFormService.resetForm(this.editForm, utilisateurCompte);

    this.utilisateursSharedCollection = this.utilisateurService.addUtilisateurToCollectionIfMissing<IUtilisateur>(
      this.utilisateursSharedCollection,
      utilisateurCompte.utilisateur,
    );
    this.comptesSharedCollection = this.compteService.addCompteToCollectionIfMissing<ICompte>(
      this.comptesSharedCollection,
      utilisateurCompte.compte,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.utilisateurService
      .query()
      .pipe(map((res: HttpResponse<IUtilisateur[]>) => res.body ?? []))
      .pipe(
        map((utilisateurs: IUtilisateur[]) =>
          this.utilisateurService.addUtilisateurToCollectionIfMissing<IUtilisateur>(utilisateurs, this.utilisateurCompte?.utilisateur),
        ),
      )
      .subscribe((utilisateurs: IUtilisateur[]) => (this.utilisateursSharedCollection = utilisateurs));

    this.compteService
      .query()
      .pipe(map((res: HttpResponse<ICompte[]>) => res.body ?? []))
      .pipe(
        map((comptes: ICompte[]) => this.compteService.addCompteToCollectionIfMissing<ICompte>(comptes, this.utilisateurCompte?.compte)),
      )
      .subscribe((comptes: ICompte[]) => (this.comptesSharedCollection = comptes));
  }
}
