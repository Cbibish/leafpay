export interface IRole {
  id: number;
  nom?: string | null;
}

export type NewRole = Omit<IRole, 'id'> & { id: null };
