import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';
import { ICompte } from 'app/entities/compte/compte.model';

export interface IUtilisateurCompte {
  id: number;
  roleUtilisateurSurCeCompte?: string | null;
  utilisateur?: Pick<IUtilisateur, 'id'> | null;
  compte?: Pick<ICompte, 'id'> | null;
}

export type NewUtilisateurCompte = Omit<IUtilisateurCompte, 'id'> & { id: null };
