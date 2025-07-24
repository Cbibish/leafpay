import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ICompte, NewCompte } from '../compte.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICompte for edit and NewCompteFormGroupInput for create.
 */
type CompteFormGroupInput = ICompte | PartialWithRequiredKeyOf<NewCompte>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ICompte | NewCompte> = Omit<T, 'dateOuverture' | 'dateFermeture'> & {
  dateOuverture?: string | null;
  dateFermeture?: string | null;
};

type CompteFormRawValue = FormValueOf<ICompte>;

type NewCompteFormRawValue = FormValueOf<NewCompte>;

type CompteFormDefaults = Pick<NewCompte, 'id' | 'dateOuverture' | 'dateFermeture'>;

type CompteFormGroupContent = {
  id: FormControl<CompteFormRawValue['id'] | NewCompte['id']>;
  typeCompte: FormControl<CompteFormRawValue['typeCompte']>;
  solde: FormControl<CompteFormRawValue['solde']>;
  plafondTransaction: FormControl<CompteFormRawValue['plafondTransaction']>;
  limiteRetraitsMensuels: FormControl<CompteFormRawValue['limiteRetraitsMensuels']>;
  tauxInteret: FormControl<CompteFormRawValue['tauxInteret']>;
  dateOuverture: FormControl<CompteFormRawValue['dateOuverture']>;
  dateFermeture: FormControl<CompteFormRawValue['dateFermeture']>;
  statut: FormControl<CompteFormRawValue['statut']>;
  iban: FormControl<CompteFormRawValue['iban']>;
};

export type CompteFormGroup = FormGroup<CompteFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CompteFormService {
  createCompteFormGroup(compte: CompteFormGroupInput = { id: null }): CompteFormGroup {
    const compteRawValue = this.convertCompteToCompteRawValue({
      ...this.getFormDefaults(),
      ...compte,
    });
    return new FormGroup<CompteFormGroupContent>({
      id: new FormControl(
        { value: compteRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      typeCompte: new FormControl(compteRawValue.typeCompte, {
        validators: [Validators.maxLength(50)],
      }),
      solde: new FormControl(compteRawValue.solde),
      plafondTransaction: new FormControl(compteRawValue.plafondTransaction),
      limiteRetraitsMensuels: new FormControl(compteRawValue.limiteRetraitsMensuels),
      tauxInteret: new FormControl(compteRawValue.tauxInteret),
      dateOuverture: new FormControl(compteRawValue.dateOuverture),
      dateFermeture: new FormControl(compteRawValue.dateFermeture),
      statut: new FormControl(compteRawValue.statut, {
        validators: [Validators.maxLength(50)],
      }),
      iban: new FormControl(compteRawValue.iban, {
        validators: [Validators.maxLength(34)],
      }),
    });
  }

  getCompte(form: CompteFormGroup): ICompte | NewCompte {
    return this.convertCompteRawValueToCompte(form.getRawValue() as CompteFormRawValue | NewCompteFormRawValue);
  }

  resetForm(form: CompteFormGroup, compte: CompteFormGroupInput): void {
    const compteRawValue = this.convertCompteToCompteRawValue({ ...this.getFormDefaults(), ...compte });
    form.reset(
      {
        ...compteRawValue,
        id: { value: compteRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CompteFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateOuverture: currentTime,
      dateFermeture: currentTime,
    };
  }

  private convertCompteRawValueToCompte(rawCompte: CompteFormRawValue | NewCompteFormRawValue): ICompte | NewCompte {
    return {
      ...rawCompte,
      dateOuverture: dayjs(rawCompte.dateOuverture, DATE_TIME_FORMAT),
      dateFermeture: dayjs(rawCompte.dateFermeture, DATE_TIME_FORMAT),
    };
  }

  private convertCompteToCompteRawValue(
    compte: ICompte | (Partial<NewCompte> & CompteFormDefaults),
  ): CompteFormRawValue | PartialWithRequiredKeyOf<NewCompteFormRawValue> {
    return {
      ...compte,
      dateOuverture: compte.dateOuverture ? compte.dateOuverture.format(DATE_TIME_FORMAT) : undefined,
      dateFermeture: compte.dateFermeture ? compte.dateFermeture.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
