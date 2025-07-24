import dayjs from 'dayjs/esm';

import { IAlerteSecurite, NewAlerteSecurite } from './alerte-securite.model';

export const sampleWithRequiredData: IAlerteSecurite = {
  id: 25959,
};

export const sampleWithPartialData: IAlerteSecurite = {
  id: 12238,
  niveauSeverite: 'scarcely eek boohoo',
  estTraitee: false,
};

export const sampleWithFullData: IAlerteSecurite = {
  id: 28296,
  typeAlerte: 'pitiful',
  niveauSeverite: 'blah ew fast',
  timestamp: dayjs('2025-07-20T14:15'),
  estTraitee: true,
};

export const sampleWithNewData: NewAlerteSecurite = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
