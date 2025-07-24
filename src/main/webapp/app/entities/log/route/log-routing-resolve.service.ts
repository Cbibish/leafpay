import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILog } from '../log.model';
import { LogService } from '../service/log.service';

const logResolve = (route: ActivatedRouteSnapshot): Observable<null | ILog> => {
  const id = route.params.id;
  if (id) {
    return inject(LogService)
      .find(id)
      .pipe(
        mergeMap((log: HttpResponse<ILog>) => {
          if (log.body) {
            return of(log.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default logResolve;
