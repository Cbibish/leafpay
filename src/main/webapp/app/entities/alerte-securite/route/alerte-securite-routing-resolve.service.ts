import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAlerteSecurite } from '../alerte-securite.model';
import { AlerteSecuriteService } from '../service/alerte-securite.service';

const alerteSecuriteResolve = (route: ActivatedRouteSnapshot): Observable<null | IAlerteSecurite> => {
  const id = route.params.id;
  if (id) {
    return inject(AlerteSecuriteService)
      .find(id)
      .pipe(
        mergeMap((alerteSecurite: HttpResponse<IAlerteSecurite>) => {
          if (alerteSecurite.body) {
            return of(alerteSecurite.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default alerteSecuriteResolve;
