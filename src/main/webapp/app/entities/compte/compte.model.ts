import dayjs from 'dayjs/esm';

export interface ICompte {
  id: number;
  typeCompte?: string | null;
  solde?: number | null;
  plafondTransaction?: number | null;
  limiteRetraitsMensuels?: number | null;
  tauxInteret?: number | null;
  dateOuverture?: dayjs.Dayjs | null;
  dateFermeture?: dayjs.Dayjs | null;
  statut?: string | null;
  iban?: string | null;
}

export type NewCompte = Omit<ICompte, 'id'> & { id: null };
