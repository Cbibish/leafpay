export class Registration {
  constructor(
    public nom: string,
    public prenom: string,
    public email: string,
    public motDePasse: string,
    public typeJustificatifAge: string,
    public role: { nom: string },
  ) {}
}
