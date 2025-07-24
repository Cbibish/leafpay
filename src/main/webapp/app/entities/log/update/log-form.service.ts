import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ILog, NewLog } from '../log.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ILog for edit and NewLogFormGroupInput for create.
 */
type LogFormGroupInput = ILog | PartialWithRequiredKeyOf<NewLog>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ILog | NewLog> = Omit<T, 'timestamp'> & {
  timestamp?: string | null;
};

type LogFormRawValue = FormValueOf<ILog>;

type NewLogFormRawValue = FormValueOf<NewLog>;

type LogFormDefaults = Pick<NewLog, 'id' | 'timestamp'>;

type LogFormGroupContent = {
  id: FormControl<LogFormRawValue['id'] | NewLog['id']>;
  action: FormControl<LogFormRawValue['action']>;
  timestamp: FormControl<LogFormRawValue['timestamp']>;
  ipUtilisateur: FormControl<LogFormRawValue['ipUtilisateur']>;
  resultat: FormControl<LogFormRawValue['resultat']>;
  description: FormControl<LogFormRawValue['description']>;
  utilisateur: FormControl<LogFormRawValue['utilisateur']>;
};

export type LogFormGroup = FormGroup<LogFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class LogFormService {
  createLogFormGroup(log: LogFormGroupInput = { id: null }): LogFormGroup {
    const logRawValue = this.convertLogToLogRawValue({
      ...this.getFormDefaults(),
      ...log,
    });
    return new FormGroup<LogFormGroupContent>({
      id: new FormControl(
        { value: logRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      action: new FormControl(logRawValue.action, {
        validators: [Validators.maxLength(255)],
      }),
      timestamp: new FormControl(logRawValue.timestamp),
      ipUtilisateur: new FormControl(logRawValue.ipUtilisateur, {
        validators: [Validators.maxLength(50)],
      }),
      resultat: new FormControl(logRawValue.resultat, {
        validators: [Validators.maxLength(50)],
      }),
      description: new FormControl(logRawValue.description),
      utilisateur: new FormControl(logRawValue.utilisateur),
    });
  }

  getLog(form: LogFormGroup): ILog | NewLog {
    return this.convertLogRawValueToLog(form.getRawValue() as LogFormRawValue | NewLogFormRawValue);
  }

  resetForm(form: LogFormGroup, log: LogFormGroupInput): void {
    const logRawValue = this.convertLogToLogRawValue({ ...this.getFormDefaults(), ...log });
    form.reset(
      {
        ...logRawValue,
        id: { value: logRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): LogFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      timestamp: currentTime,
    };
  }

  private convertLogRawValueToLog(rawLog: LogFormRawValue | NewLogFormRawValue): ILog | NewLog {
    return {
      ...rawLog,
      timestamp: dayjs(rawLog.timestamp, DATE_TIME_FORMAT),
    };
  }

  private convertLogToLogRawValue(
    log: ILog | (Partial<NewLog> & LogFormDefaults),
  ): LogFormRawValue | PartialWithRequiredKeyOf<NewLogFormRawValue> {
    return {
      ...log,
      timestamp: log.timestamp ? log.timestamp.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
