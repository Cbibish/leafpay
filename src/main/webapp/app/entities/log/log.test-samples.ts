import dayjs from 'dayjs/esm';

import { ILog, NewLog } from './log.model';

export const sampleWithRequiredData: ILog = {
  id: 12215,
};

export const sampleWithPartialData: ILog = {
  id: 1063,
  action: 'vengeful unpleasant',
  ipUtilisateur: 'essential',
  description: '../fake-data/blob/hipster.txt',
};

export const sampleWithFullData: ILog = {
  id: 22983,
  action: 'dwell',
  timestamp: dayjs('2025-07-19T22:45'),
  ipUtilisateur: 'eek bakeware certainly',
  resultat: 'lest although so',
  description: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewLog = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
