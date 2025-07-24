import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUtilisateur, NewUtilisateur } from '../utilisateur.model';

export type PartialUpdateUtilisateur = Partial<IUtilisateur> & Pick<IUtilisateur, 'id'>;

type RestOf<T extends IUtilisateur | NewUtilisateur> = Omit<T, 'dateNaissance' | 'dateCreation'> & {
  dateNaissance?: string | null;
  dateCreation?: string | null;
};

export type RestUtilisateur = RestOf<IUtilisateur>;

export type NewRestUtilisateur = RestOf<NewUtilisateur>;

export type PartialUpdateRestUtilisateur = RestOf<PartialUpdateUtilisateur>;

export type EntityResponseType = HttpResponse<IUtilisateur>;
export type EntityArrayResponseType = HttpResponse<IUtilisateur[]>;

@Injectable({ providedIn: 'root' })
export class UtilisateurService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/utilisateurs');

  create(utilisateur: NewUtilisateur): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(utilisateur);
    return this.http
      .post<RestUtilisateur>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(utilisateur: IUtilisateur): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(utilisateur);
    return this.http
      .put<RestUtilisateur>(`${this.resourceUrl}/${this.getUtilisateurIdentifier(utilisateur)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(utilisateur: PartialUpdateUtilisateur): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(utilisateur);
    return this.http
      .patch<RestUtilisateur>(`${this.resourceUrl}/${this.getUtilisateurIdentifier(utilisateur)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestUtilisateur>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestUtilisateur[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getUtilisateurIdentifier(utilisateur: Pick<IUtilisateur, 'id'>): number {
    return utilisateur.id;
  }

  compareUtilisateur(o1: Pick<IUtilisateur, 'id'> | null, o2: Pick<IUtilisateur, 'id'> | null): boolean {
    return o1 && o2 ? this.getUtilisateurIdentifier(o1) === this.getUtilisateurIdentifier(o2) : o1 === o2;
  }

  addUtilisateurToCollectionIfMissing<Type extends Pick<IUtilisateur, 'id'>>(
    utilisateurCollection: Type[],
    ...utilisateursToCheck: (Type | null | undefined)[]
  ): Type[] {
    const utilisateurs: Type[] = utilisateursToCheck.filter(isPresent);
    if (utilisateurs.length > 0) {
      const utilisateurCollectionIdentifiers = utilisateurCollection.map(utilisateurItem => this.getUtilisateurIdentifier(utilisateurItem));
      const utilisateursToAdd = utilisateurs.filter(utilisateurItem => {
        const utilisateurIdentifier = this.getUtilisateurIdentifier(utilisateurItem);
        if (utilisateurCollectionIdentifiers.includes(utilisateurIdentifier)) {
          return false;
        }
        utilisateurCollectionIdentifiers.push(utilisateurIdentifier);
        return true;
      });
      return [...utilisateursToAdd, ...utilisateurCollection];
    }
    return utilisateurCollection;
  }

  protected convertDateFromClient<T extends IUtilisateur | NewUtilisateur | PartialUpdateUtilisateur>(utilisateur: T): RestOf<T> {
    return {
      ...utilisateur,
      dateNaissance: utilisateur.dateNaissance?.format(DATE_FORMAT) ?? null,
      dateCreation: utilisateur.dateCreation?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restUtilisateur: RestUtilisateur): IUtilisateur {
    return {
      ...restUtilisateur,
      dateNaissance: restUtilisateur.dateNaissance ? dayjs(restUtilisateur.dateNaissance) : undefined,
      dateCreation: restUtilisateur.dateCreation ? dayjs(restUtilisateur.dateCreation) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestUtilisateur>): HttpResponse<IUtilisateur> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestUtilisateur[]>): HttpResponse<IUtilisateur[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
