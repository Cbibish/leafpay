import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import UtilisateurCompteResolve from './route/utilisateur-compte-routing-resolve.service';

const utilisateurCompteRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/utilisateur-compte.component').then(m => m.UtilisateurCompteComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/utilisateur-compte-detail.component').then(m => m.UtilisateurCompteDetailComponent),
    resolve: {
      utilisateurCompte: UtilisateurCompteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/utilisateur-compte-update.component').then(m => m.UtilisateurCompteUpdateComponent),
    resolve: {
      utilisateurCompte: UtilisateurCompteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/utilisateur-compte-update.component').then(m => m.UtilisateurCompteUpdateComponent),
    resolve: {
      utilisateurCompte: UtilisateurCompteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default utilisateurCompteRoute;
