import { Routes } from '@angular/router';

import { Authority } from 'app/config/authority.constants';
import accountRoutes from './account/account.route';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { errorRoute } from './layouts/error/error.route';

const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./home/home.component'),
    title: 'home.title',
  },
  {
    path: '',
    loadComponent: () => import('./layouts/navbar/navbar.component'),
    outlet: 'navbar',
  },
  {
    path: 'admin',
    data: {
      authorities: [Authority.ADMIN],
    },
    canActivate: [UserRouteAccessService],
    loadChildren: () => import('./admin/admin.routes'),
  },
  {
    path: 'login',
    loadComponent: () => import('./login/login.component'),
    title: 'login.title',
  },
  {
    path: '',
    loadChildren: () => import(`./entities/entity.routes`),
  },
  {
    path: 'client-dashboard',
    loadComponent: () => import(`./client-dashboard/client-dashboard.component`).then(m => m.ClientDashboardComponent),
    title: 'client-dashboard.title',
  },
  {
    path: 'account',
    children: accountRoutes,
  },
  ...errorRoute,
];

export default routes;
