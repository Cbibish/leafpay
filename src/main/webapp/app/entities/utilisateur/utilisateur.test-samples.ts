import dayjs from 'dayjs/esm';

import { IUtilisateur, NewUtilisateur } from './utilisateur.model';

export const sampleWithRequiredData: IUtilisateur = {
  id: 7635,
  nom: 'whirlwind optimistically',
  prenom: 'inwardly',
  email: 'Elisha.Veum@gmail.com',
  motDePasse: 'snappy blank toward',
};

export const sampleWithPartialData: IUtilisateur = {
  id: 12643,
  nom: 'jaggedly delectable consequently',
  prenom: 'considering lest',
  email: 'Delaney_Dicki-Hills@gmail.com',
  motDePasse: 'airbus up',
};

export const sampleWithFullData: IUtilisateur = {
  id: 18250,
  nom: 'since across',
  prenom: 'sizzle',
  email: 'Violette.Schiller@hotmail.com',
  motDePasse: 'woot',
  dateNaissance: dayjs('2025-07-20'),
  typeJustificatifAge: 'once marimba nor',
  statut: 'miserably',
  dateCreation: dayjs('2025-07-20T19:14'),
};

export const sampleWithNewData: NewUtilisateur = {
  nom: 'hot',
  prenom: 'even coarse',
  email: 'Mary_Prohaska@yahoo.com',
  motDePasse: 'recent regal',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
