import dayjs from 'dayjs/esm';

import { ICompte, NewCompte } from './compte.model';

export const sampleWithRequiredData: ICompte = {
  id: 9105,
};

export const sampleWithPartialData: ICompte = {
  id: 26465,
  solde: 5798.86,
  limiteRetraitsMensuels: 10661,
  tauxInteret: 32617.51,
  dateFermeture: dayjs('2025-07-20T02:25'),
  statut: 'profane',
  iban: 'SK0967095800183170010014',
};

export const sampleWithFullData: ICompte = {
  id: 32146,
  typeCompte: 'joint mid',
  solde: 13116.42,
  plafondTransaction: 2514.86,
  limiteRetraitsMensuels: 16396,
  tauxInteret: 5251.56,
  dateOuverture: dayjs('2025-07-20T06:51'),
  dateFermeture: dayjs('2025-07-20T19:28'),
  statut: 'oof',
  iban: 'BG42JEPZ890825K970MQ99',
};

export const sampleWithNewData: NewCompte = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
