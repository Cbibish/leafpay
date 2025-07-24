import { IUtilisateurCompte, NewUtilisateurCompte } from './utilisateur-compte.model';

export const sampleWithRequiredData: IUtilisateurCompte = {
  id: 18247,
};

export const sampleWithPartialData: IUtilisateurCompte = {
  id: 12776,
  roleUtilisateurSurCeCompte: 'shark shrill too',
};

export const sampleWithFullData: IUtilisateurCompte = {
  id: 5740,
  roleUtilisateurSurCeCompte: 'drat',
};

export const sampleWithNewData: NewUtilisateurCompte = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
