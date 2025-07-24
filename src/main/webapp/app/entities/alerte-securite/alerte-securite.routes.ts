import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import AlerteSecuriteResolve from './route/alerte-securite-routing-resolve.service';

const alerteSecuriteRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/alerte-securite.component').then(m => m.AlerteSecuriteComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/alerte-securite-detail.component').then(m => m.AlerteSecuriteDetailComponent),
    resolve: {
      alerteSecurite: AlerteSecuriteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/alerte-securite-update.component').then(m => m.AlerteSecuriteUpdateComponent),
    resolve: {
      alerteSecurite: AlerteSecuriteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/alerte-securite-update.component').then(m => m.AlerteSecuriteUpdateComponent),
    resolve: {
      alerteSecurite: AlerteSecuriteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default alerteSecuriteRoute;
