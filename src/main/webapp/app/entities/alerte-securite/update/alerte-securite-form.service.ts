import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IAlerteSecurite, NewAlerteSecurite } from '../alerte-securite.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAlerteSecurite for edit and NewAlerteSecuriteFormGroupInput for create.
 */
type AlerteSecuriteFormGroupInput = IAlerteSecurite | PartialWithRequiredKeyOf<NewAlerteSecurite>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IAlerteSecurite | NewAlerteSecurite> = Omit<T, 'timestamp'> & {
  timestamp?: string | null;
};

type AlerteSecuriteFormRawValue = FormValueOf<IAlerteSecurite>;

type NewAlerteSecuriteFormRawValue = FormValueOf<NewAlerteSecurite>;

type AlerteSecuriteFormDefaults = Pick<NewAlerteSecurite, 'id' | 'timestamp' | 'estTraitee'>;

type AlerteSecuriteFormGroupContent = {
  id: FormControl<AlerteSecuriteFormRawValue['id'] | NewAlerteSecurite['id']>;
  typeAlerte: FormControl<AlerteSecuriteFormRawValue['typeAlerte']>;
  niveauSeverite: FormControl<AlerteSecuriteFormRawValue['niveauSeverite']>;
  timestamp: FormControl<AlerteSecuriteFormRawValue['timestamp']>;
  estTraitee: FormControl<AlerteSecuriteFormRawValue['estTraitee']>;
  utilisateur: FormControl<AlerteSecuriteFormRawValue['utilisateur']>;
};

export type AlerteSecuriteFormGroup = FormGroup<AlerteSecuriteFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AlerteSecuriteFormService {
  createAlerteSecuriteFormGroup(alerteSecurite: AlerteSecuriteFormGroupInput = { id: null }): AlerteSecuriteFormGroup {
    const alerteSecuriteRawValue = this.convertAlerteSecuriteToAlerteSecuriteRawValue({
      ...this.getFormDefaults(),
      ...alerteSecurite,
    });
    return new FormGroup<AlerteSecuriteFormGroupContent>({
      id: new FormControl(
        { value: alerteSecuriteRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      typeAlerte: new FormControl(alerteSecuriteRawValue.typeAlerte, {
        validators: [Validators.maxLength(100)],
      }),
      niveauSeverite: new FormControl(alerteSecuriteRawValue.niveauSeverite, {
        validators: [Validators.maxLength(50)],
      }),
      timestamp: new FormControl(alerteSecuriteRawValue.timestamp),
      estTraitee: new FormControl(alerteSecuriteRawValue.estTraitee),
      utilisateur: new FormControl(alerteSecuriteRawValue.utilisateur),
    });
  }

  getAlerteSecurite(form: AlerteSecuriteFormGroup): IAlerteSecurite | NewAlerteSecurite {
    return this.convertAlerteSecuriteRawValueToAlerteSecurite(
      form.getRawValue() as AlerteSecuriteFormRawValue | NewAlerteSecuriteFormRawValue,
    );
  }

  resetForm(form: AlerteSecuriteFormGroup, alerteSecurite: AlerteSecuriteFormGroupInput): void {
    const alerteSecuriteRawValue = this.convertAlerteSecuriteToAlerteSecuriteRawValue({ ...this.getFormDefaults(), ...alerteSecurite });
    form.reset(
      {
        ...alerteSecuriteRawValue,
        id: { value: alerteSecuriteRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AlerteSecuriteFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      timestamp: currentTime,
      estTraitee: false,
    };
  }

  private convertAlerteSecuriteRawValueToAlerteSecurite(
    rawAlerteSecurite: AlerteSecuriteFormRawValue | NewAlerteSecuriteFormRawValue,
  ): IAlerteSecurite | NewAlerteSecurite {
    return {
      ...rawAlerteSecurite,
      timestamp: dayjs(rawAlerteSecurite.timestamp, DATE_TIME_FORMAT),
    };
  }

  private convertAlerteSecuriteToAlerteSecuriteRawValue(
    alerteSecurite: IAlerteSecurite | (Partial<NewAlerteSecurite> & AlerteSecuriteFormDefaults),
  ): AlerteSecuriteFormRawValue | PartialWithRequiredKeyOf<NewAlerteSecuriteFormRawValue> {
    return {
      ...alerteSecurite,
      timestamp: alerteSecurite.timestamp ? alerteSecurite.timestamp.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
