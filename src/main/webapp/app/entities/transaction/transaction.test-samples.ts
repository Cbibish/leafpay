import dayjs from 'dayjs/esm';

import { ITransaction, NewTransaction } from './transaction.model';

export const sampleWithRequiredData: ITransaction = {
  id: 3793,
};

export const sampleWithPartialData: ITransaction = {
  id: 19830,
  typeTransaction: 'angelic polished emulsify',
  statut: 'qualified',
  moyenValidation: 'consequently substantiate',
};

export const sampleWithFullData: ITransaction = {
  id: 20285,
  montant: 600.71,
  typeTransaction: 'qua hm into',
  dateTransaction: dayjs('2025-07-20T20:38'),
  statut: 'duh since rightfully',
  moyenValidation: 'elliptical',
  justificatif: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewTransaction = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
