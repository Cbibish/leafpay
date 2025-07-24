import { IRole, NewRole } from './role.model';

export const sampleWithRequiredData: IRole = {
  id: 2153,
  nom: 'outgoing unblinking',
};

export const sampleWithPartialData: IRole = {
  id: 11844,
  nom: 'pink inside leading',
};

export const sampleWithFullData: IRole = {
  id: 16604,
  nom: 'before solidly swill',
};

export const sampleWithNewData: NewRole = {
  nom: 'commonly writhing than',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
