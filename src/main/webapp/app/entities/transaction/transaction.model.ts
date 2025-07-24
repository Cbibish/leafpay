import dayjs from 'dayjs/esm';
import { ICompte } from 'app/entities/compte/compte.model';

export interface ITransaction {
  id: number;
  montant?: number | null;
  typeTransaction?: string | null;
  dateTransaction?: dayjs.Dayjs | null;
  statut?: string | null;
  moyenValidation?: string | null;
  justificatif?: string | null;
  compteSource?: Pick<ICompte, 'id'> | null;
  compteDestination?: Pick<ICompte, 'id'> | null;
}

export type NewTransaction = Omit<ITransaction, 'id'> & { id: null };
