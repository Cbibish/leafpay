import dayjs from 'dayjs/esm';
import { IRole } from 'app/entities/role/role.model';

export interface IUtilisateur {
  id: number;
  nom?: string | null;
  prenom?: string | null;
  email?: string | null;
  motDePasse?: string | null;
  dateNaissance?: dayjs.Dayjs | null;
  typeJustificatifAge?: string | null;
  statut?: string | null;
  dateCreation?: dayjs.Dayjs | null;
  setRole?: Pick<IRole, 'id'> | null;
}

export type NewUtilisateur = Omit<IUtilisateur, 'id'> & { id: null };
