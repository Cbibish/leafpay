import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'role',
    data: { pageTitle: 'leafpayApp.role.home.title' },
    loadChildren: () => import('./role/role.routes'),
  },
  {
    path: 'utilisateur',
    data: { pageTitle: 'leafpayApp.utilisateur.home.title' },
    loadChildren: () => import('./utilisateur/utilisateur.routes'),
  },
  {
    path: 'compte',
    data: { pageTitle: 'leafpayApp.compte.home.title' },
    loadChildren: () => import('./compte/compte.routes'),
  },
  {
    path: 'utilisateur-compte',
    data: { pageTitle: 'leafpayApp.utilisateurCompte.home.title' },
    loadChildren: () => import('./utilisateur-compte/utilisateur-compte.routes'),
  },
  {
    path: 'transaction',
    data: { pageTitle: 'leafpayApp.transaction.home.title' },
    loadChildren: () => import('./transaction/transaction.routes'),
  },
  {
    path: 'log',
    data: { pageTitle: 'leafpayApp.log.home.title' },
    loadChildren: () => import('./log/log.routes'),
  },
  {
    path: 'alerte-securite',
    data: { pageTitle: 'leafpayApp.alerteSecurite.home.title' },
    loadChildren: () => import('./alerte-securite/alerte-securite.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
