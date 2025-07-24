import dayjs from 'dayjs/esm';
import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';

export interface ILog {
  id: number;
  action?: string | null;
  timestamp?: dayjs.Dayjs | null;
  ipUtilisateur?: string | null;
  resultat?: string | null;
  description?: string | null;
  utilisateur?: Pick<IUtilisateur, 'id'> | null;
}

export type NewLog = Omit<ILog, 'id'> & { id: null };
