import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IUtilisateur, NewUtilisateur } from '../utilisateur.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IUtilisateur for edit and NewUtilisateurFormGroupInput for create.
 */
type UtilisateurFormGroupInput = IUtilisateur | PartialWithRequiredKeyOf<NewUtilisateur>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IUtilisateur | NewUtilisateur> = Omit<T, 'dateCreation'> & {
  dateCreation?: string | null;
};

type UtilisateurFormRawValue = FormValueOf<IUtilisateur>;

type NewUtilisateurFormRawValue = FormValueOf<NewUtilisateur>;

type UtilisateurFormDefaults = Pick<NewUtilisateur, 'id' | 'dateCreation'>;

type UtilisateurFormGroupContent = {
  id: FormControl<UtilisateurFormRawValue['id'] | NewUtilisateur['id']>;
  nom: FormControl<UtilisateurFormRawValue['nom']>;
  prenom: FormControl<UtilisateurFormRawValue['prenom']>;
  email: FormControl<UtilisateurFormRawValue['email']>;
  motDePasse: FormControl<UtilisateurFormRawValue['motDePasse']>;
  dateNaissance: FormControl<UtilisateurFormRawValue['dateNaissance']>;
  typeJustificatifAge: FormControl<UtilisateurFormRawValue['typeJustificatifAge']>;
  statut: FormControl<UtilisateurFormRawValue['statut']>;
  dateCreation: FormControl<UtilisateurFormRawValue['dateCreation']>;
  setRole: FormControl<UtilisateurFormRawValue['setRole']>;
};

export type UtilisateurFormGroup = FormGroup<UtilisateurFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class UtilisateurFormService {
  createUtilisateurFormGroup(utilisateur: UtilisateurFormGroupInput = { id: null }): UtilisateurFormGroup {
    const utilisateurRawValue = this.convertUtilisateurToUtilisateurRawValue({
      ...this.getFormDefaults(),
      ...utilisateur,
    });
    return new FormGroup<UtilisateurFormGroupContent>({
      id: new FormControl(
        { value: utilisateurRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nom: new FormControl(utilisateurRawValue.nom, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      prenom: new FormControl(utilisateurRawValue.prenom, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      email: new FormControl(utilisateurRawValue.email, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      motDePasse: new FormControl(utilisateurRawValue.motDePasse, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      dateNaissance: new FormControl(utilisateurRawValue.dateNaissance),
      typeJustificatifAge: new FormControl(utilisateurRawValue.typeJustificatifAge, {
        validators: [Validators.maxLength(255)],
      }),
      statut: new FormControl(utilisateurRawValue.statut, {
        validators: [Validators.maxLength(50)],
      }),
      dateCreation: new FormControl(utilisateurRawValue.dateCreation),
      setRole: new FormControl(utilisateurRawValue.setRole),
    });
  }

  getUtilisateur(form: UtilisateurFormGroup): IUtilisateur | NewUtilisateur {
    return this.convertUtilisateurRawValueToUtilisateur(form.getRawValue() as UtilisateurFormRawValue | NewUtilisateurFormRawValue);
  }

  resetForm(form: UtilisateurFormGroup, utilisateur: UtilisateurFormGroupInput): void {
    const utilisateurRawValue = this.convertUtilisateurToUtilisateurRawValue({ ...this.getFormDefaults(), ...utilisateur });
    form.reset(
      {
        ...utilisateurRawValue,
        id: { value: utilisateurRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): UtilisateurFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateCreation: currentTime,
    };
  }

  private convertUtilisateurRawValueToUtilisateur(
    rawUtilisateur: UtilisateurFormRawValue | NewUtilisateurFormRawValue,
  ): IUtilisateur | NewUtilisateur {
    return {
      ...rawUtilisateur,
      dateCreation: dayjs(rawUtilisateur.dateCreation, DATE_TIME_FORMAT),
    };
  }

  private convertUtilisateurToUtilisateurRawValue(
    utilisateur: IUtilisateur | (Partial<NewUtilisateur> & UtilisateurFormDefaults),
  ): UtilisateurFormRawValue | PartialWithRequiredKeyOf<NewUtilisateurFormRawValue> {
    return {
      ...utilisateur,
      dateCreation: utilisateur.dateCreation ? utilisateur.dateCreation.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
