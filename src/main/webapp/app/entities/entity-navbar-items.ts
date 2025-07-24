import NavbarItem from 'app/layouts/navbar/navbar-item.model';

export const EntityNavbarItems: NavbarItem[] = [
  {
    name: 'Role',
    route: '/role',
    translationKey: 'global.menu.entities.role',
  },
  {
    name: 'Utilisateur',
    route: '/utilisateur',
    translationKey: 'global.menu.entities.utilisateur',
  },
  {
    name: 'Compte',
    route: '/compte',
    translationKey: 'global.menu.entities.compte',
  },
  {
    name: 'UtilisateurCompte',
    route: '/utilisateur-compte',
    translationKey: 'global.menu.entities.utilisateurCompte',
  },
  {
    name: 'Transaction',
    route: '/transaction',
    translationKey: 'global.menu.entities.transaction',
  },
  {
    name: 'Log',
    route: '/log',
    translationKey: 'global.menu.entities.log',
  },
  {
    name: 'AlerteSecurite',
    route: '/alerte-securite',
    translationKey: 'global.menu.entities.alerteSecurite',
  },
];
