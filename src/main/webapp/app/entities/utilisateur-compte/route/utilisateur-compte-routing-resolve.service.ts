import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IUtilisateurCompte } from '../utilisateur-compte.model';
import { UtilisateurCompteService } from '../service/utilisateur-compte.service';

const utilisateurCompteResolve = (route: ActivatedRouteSnapshot): Observable<null | IUtilisateurCompte> => {
  const id = route.params.id;
  if (id) {
    return inject(UtilisateurCompteService)
      .find(id)
      .pipe(
        mergeMap((utilisateurCompte: HttpResponse<IUtilisateurCompte>) => {
          if (utilisateurCompte.body) {
            return of(utilisateurCompte.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default utilisateurCompteResolve;
