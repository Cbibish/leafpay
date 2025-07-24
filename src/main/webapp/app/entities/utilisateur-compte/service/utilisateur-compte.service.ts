import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUtilisateurCompte, NewUtilisateurCompte } from '../utilisateur-compte.model';

export type PartialUpdateUtilisateurCompte = Partial<IUtilisateurCompte> & Pick<IUtilisateurCompte, 'id'>;

export type EntityResponseType = HttpResponse<IUtilisateurCompte>;
export type EntityArrayResponseType = HttpResponse<IUtilisateurCompte[]>;

@Injectable({ providedIn: 'root' })
export class UtilisateurCompteService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/utilisateur-comptes');

  create(utilisateurCompte: NewUtilisateurCompte): Observable<EntityResponseType> {
    return this.http.post<IUtilisateurCompte>(this.resourceUrl, utilisateurCompte, { observe: 'response' });
  }

  update(utilisateurCompte: IUtilisateurCompte): Observable<EntityResponseType> {
    return this.http.put<IUtilisateurCompte>(
      `${this.resourceUrl}/${this.getUtilisateurCompteIdentifier(utilisateurCompte)}`,
      utilisateurCompte,
      { observe: 'response' },
    );
  }

  partialUpdate(utilisateurCompte: PartialUpdateUtilisateurCompte): Observable<EntityResponseType> {
    return this.http.patch<IUtilisateurCompte>(
      `${this.resourceUrl}/${this.getUtilisateurCompteIdentifier(utilisateurCompte)}`,
      utilisateurCompte,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IUtilisateurCompte>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IUtilisateurCompte[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getUtilisateurCompteIdentifier(utilisateurCompte: Pick<IUtilisateurCompte, 'id'>): number {
    return utilisateurCompte.id;
  }

  compareUtilisateurCompte(o1: Pick<IUtilisateurCompte, 'id'> | null, o2: Pick<IUtilisateurCompte, 'id'> | null): boolean {
    return o1 && o2 ? this.getUtilisateurCompteIdentifier(o1) === this.getUtilisateurCompteIdentifier(o2) : o1 === o2;
  }

  addUtilisateurCompteToCollectionIfMissing<Type extends Pick<IUtilisateurCompte, 'id'>>(
    utilisateurCompteCollection: Type[],
    ...utilisateurComptesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const utilisateurComptes: Type[] = utilisateurComptesToCheck.filter(isPresent);
    if (utilisateurComptes.length > 0) {
      const utilisateurCompteCollectionIdentifiers = utilisateurCompteCollection.map(utilisateurCompteItem =>
        this.getUtilisateurCompteIdentifier(utilisateurCompteItem),
      );
      const utilisateurComptesToAdd = utilisateurComptes.filter(utilisateurCompteItem => {
        const utilisateurCompteIdentifier = this.getUtilisateurCompteIdentifier(utilisateurCompteItem);
        if (utilisateurCompteCollectionIdentifiers.includes(utilisateurCompteIdentifier)) {
          return false;
        }
        utilisateurCompteCollectionIdentifiers.push(utilisateurCompteIdentifier);
        return true;
      });
      return [...utilisateurComptesToAdd, ...utilisateurCompteCollection];
    }
    return utilisateurCompteCollection;
  }
}
