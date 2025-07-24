import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ITransaction, NewTransaction } from '../transaction.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITransaction for edit and NewTransactionFormGroupInput for create.
 */
type TransactionFormGroupInput = ITransaction | PartialWithRequiredKeyOf<NewTransaction>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ITransaction | NewTransaction> = Omit<T, 'dateTransaction'> & {
  dateTransaction?: string | null;
};

type TransactionFormRawValue = FormValueOf<ITransaction>;

type NewTransactionFormRawValue = FormValueOf<NewTransaction>;

type TransactionFormDefaults = Pick<NewTransaction, 'id' | 'dateTransaction'>;

type TransactionFormGroupContent = {
  id: FormControl<TransactionFormRawValue['id'] | NewTransaction['id']>;
  montant: FormControl<TransactionFormRawValue['montant']>;
  typeTransaction: FormControl<TransactionFormRawValue['typeTransaction']>;
  dateTransaction: FormControl<TransactionFormRawValue['dateTransaction']>;
  statut: FormControl<TransactionFormRawValue['statut']>;
  moyenValidation: FormControl<TransactionFormRawValue['moyenValidation']>;
  justificatif: FormControl<TransactionFormRawValue['justificatif']>;
  compteSource: FormControl<TransactionFormRawValue['compteSource']>;
  compteDestination: FormControl<TransactionFormRawValue['compteDestination']>;
};

export type TransactionFormGroup = FormGroup<TransactionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TransactionFormService {
  createTransactionFormGroup(transaction: TransactionFormGroupInput = { id: null }): TransactionFormGroup {
    const transactionRawValue = this.convertTransactionToTransactionRawValue({
      ...this.getFormDefaults(),
      ...transaction,
    });
    return new FormGroup<TransactionFormGroupContent>({
      id: new FormControl(
        { value: transactionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      montant: new FormControl(transactionRawValue.montant),
      typeTransaction: new FormControl(transactionRawValue.typeTransaction, {
        validators: [Validators.maxLength(50)],
      }),
      dateTransaction: new FormControl(transactionRawValue.dateTransaction),
      statut: new FormControl(transactionRawValue.statut, {
        validators: [Validators.maxLength(50)],
      }),
      moyenValidation: new FormControl(transactionRawValue.moyenValidation, {
        validators: [Validators.maxLength(255)],
      }),
      justificatif: new FormControl(transactionRawValue.justificatif),
      compteSource: new FormControl(transactionRawValue.compteSource),
      compteDestination: new FormControl(transactionRawValue.compteDestination),
    });
  }

  getTransaction(form: TransactionFormGroup): ITransaction | NewTransaction {
    return this.convertTransactionRawValueToTransaction(form.getRawValue() as TransactionFormRawValue | NewTransactionFormRawValue);
  }

  resetForm(form: TransactionFormGroup, transaction: TransactionFormGroupInput): void {
    const transactionRawValue = this.convertTransactionToTransactionRawValue({ ...this.getFormDefaults(), ...transaction });
    form.reset(
      {
        ...transactionRawValue,
        id: { value: transactionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TransactionFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateTransaction: currentTime,
    };
  }

  private convertTransactionRawValueToTransaction(
    rawTransaction: TransactionFormRawValue | NewTransactionFormRawValue,
  ): ITransaction | NewTransaction {
    return {
      ...rawTransaction,
      dateTransaction: dayjs(rawTransaction.dateTransaction, DATE_TIME_FORMAT),
    };
  }

  private convertTransactionToTransactionRawValue(
    transaction: ITransaction | (Partial<NewTransaction> & TransactionFormDefaults),
  ): TransactionFormRawValue | PartialWithRequiredKeyOf<NewTransactionFormRawValue> {
    return {
      ...transaction,
      dateTransaction: transaction.dateTransaction ? transaction.dateTransaction.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
