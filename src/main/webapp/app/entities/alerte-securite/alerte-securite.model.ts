import dayjs from 'dayjs/esm';
import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';

export interface IAlerteSecurite {
  id: number;
  typeAlerte?: string | null;
  niveauSeverite?: string | null;
  timestamp?: dayjs.Dayjs | null;
  estTraitee?: boolean | null;
  utilisateur?: Pick<IUtilisateur, 'id'> | null;
}

export type NewAlerteSecurite = Omit<IAlerteSecurite, 'id'> & { id: null };
