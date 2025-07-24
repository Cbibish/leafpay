import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IUtilisateurCompte, NewUtilisateurCompte } from '../utilisateur-compte.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IUtilisateurCompte for edit and NewUtilisateurCompteFormGroupInput for create.
 */
type UtilisateurCompteFormGroupInput = IUtilisateurCompte | PartialWithRequiredKeyOf<NewUtilisateurCompte>;

type UtilisateurCompteFormDefaults = Pick<NewUtilisateurCompte, 'id'>;

type UtilisateurCompteFormGroupContent = {
  id: FormControl<IUtilisateurCompte['id'] | NewUtilisateurCompte['id']>;
  roleUtilisateurSurCeCompte: FormControl<IUtilisateurCompte['roleUtilisateurSurCeCompte']>;
  utilisateur: FormControl<IUtilisateurCompte['utilisateur']>;
  compte: FormControl<IUtilisateurCompte['compte']>;
};

export type UtilisateurCompteFormGroup = FormGroup<UtilisateurCompteFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class UtilisateurCompteFormService {
  createUtilisateurCompteFormGroup(utilisateurCompte: UtilisateurCompteFormGroupInput = { id: null }): UtilisateurCompteFormGroup {
    const utilisateurCompteRawValue = {
      ...this.getFormDefaults(),
      ...utilisateurCompte,
    };
    return new FormGroup<UtilisateurCompteFormGroupContent>({
      id: new FormControl(
        { value: utilisateurCompteRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      roleUtilisateurSurCeCompte: new FormControl(utilisateurCompteRawValue.roleUtilisateurSurCeCompte, {
        validators: [Validators.maxLength(100)],
      }),
      utilisateur: new FormControl(utilisateurCompteRawValue.utilisateur),
      compte: new FormControl(utilisateurCompteRawValue.compte),
    });
  }

  getUtilisateurCompte(form: UtilisateurCompteFormGroup): IUtilisateurCompte | NewUtilisateurCompte {
    return form.getRawValue() as IUtilisateurCompte | NewUtilisateurCompte;
  }

  resetForm(form: UtilisateurCompteFormGroup, utilisateurCompte: UtilisateurCompteFormGroupInput): void {
    const utilisateurCompteRawValue = { ...this.getFormDefaults(), ...utilisateurCompte };
    form.reset(
      {
        ...utilisateurCompteRawValue,
        id: { value: utilisateurCompteRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): UtilisateurCompteFormDefaults {
    return {
      id: null,
    };
  }
}
