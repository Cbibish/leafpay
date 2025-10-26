export class Account {
  constructor(
    public id?: number,
    public nom?: string,
    public prenom?: string,
    public email?: string,
    public motDePasse?: string,
    public dateNaissance?: string | null,
    public typeJustificatifAge?: string,
    public statut?: string,
    public dateCreation?: string | null,
    public role?: { id: number; nom: string },
    public activated?: boolean,
    public authorities: string[] = [],
    public firstName?: string | null,
    public langKey = 'en',
    public lastName?: string | null,
    public login?: string,
    public imageUrl?: string | null,
  ) {}
}
