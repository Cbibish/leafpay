import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import LogResolve from './route/log-routing-resolve.service';

const logRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/log.component').then(m => m.LogComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/log-detail.component').then(m => m.LogDetailComponent),
    resolve: {
      log: LogResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/log-update.component').then(m => m.LogUpdateComponent),
    resolve: {
      log: LogResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/log-update.component').then(m => m.LogUpdateComponent),
    resolve: {
      log: LogResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default logRoute;
