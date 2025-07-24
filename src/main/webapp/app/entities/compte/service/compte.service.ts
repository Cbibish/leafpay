import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICompte, NewCompte } from '../compte.model';

export type PartialUpdateCompte = Partial<ICompte> & Pick<ICompte, 'id'>;

type RestOf<T extends ICompte | NewCompte> = Omit<T, 'dateOuverture' | 'dateFermeture'> & {
  dateOuverture?: string | null;
  dateFermeture?: string | null;
};

export type RestCompte = RestOf<ICompte>;

export type NewRestCompte = RestOf<NewCompte>;

export type PartialUpdateRestCompte = RestOf<PartialUpdateCompte>;

export type EntityResponseType = HttpResponse<ICompte>;
export type EntityArrayResponseType = HttpResponse<ICompte[]>;

@Injectable({ providedIn: 'root' })
export class CompteService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/comptes');

  create(compte: NewCompte): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(compte);
    return this.http
      .post<RestCompte>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(compte: ICompte): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(compte);
    return this.http
      .put<RestCompte>(`${this.resourceUrl}/${this.getCompteIdentifier(compte)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(compte: PartialUpdateCompte): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(compte);
    return this.http
      .patch<RestCompte>(`${this.resourceUrl}/${this.getCompteIdentifier(compte)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestCompte>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestCompte[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCompteIdentifier(compte: Pick<ICompte, 'id'>): number {
    return compte.id;
  }

  compareCompte(o1: Pick<ICompte, 'id'> | null, o2: Pick<ICompte, 'id'> | null): boolean {
    return o1 && o2 ? this.getCompteIdentifier(o1) === this.getCompteIdentifier(o2) : o1 === o2;
  }

  addCompteToCollectionIfMissing<Type extends Pick<ICompte, 'id'>>(
    compteCollection: Type[],
    ...comptesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const comptes: Type[] = comptesToCheck.filter(isPresent);
    if (comptes.length > 0) {
      const compteCollectionIdentifiers = compteCollection.map(compteItem => this.getCompteIdentifier(compteItem));
      const comptesToAdd = comptes.filter(compteItem => {
        const compteIdentifier = this.getCompteIdentifier(compteItem);
        if (compteCollectionIdentifiers.includes(compteIdentifier)) {
          return false;
        }
        compteCollectionIdentifiers.push(compteIdentifier);
        return true;
      });
      return [...comptesToAdd, ...compteCollection];
    }
    return compteCollection;
  }

  protected convertDateFromClient<T extends ICompte | NewCompte | PartialUpdateCompte>(compte: T): RestOf<T> {
    return {
      ...compte,
      dateOuverture: compte.dateOuverture?.toJSON() ?? null,
      dateFermeture: compte.dateFermeture?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restCompte: RestCompte): ICompte {
    return {
      ...restCompte,
      dateOuverture: restCompte.dateOuverture ? dayjs(restCompte.dateOuverture) : undefined,
      dateFermeture: restCompte.dateFermeture ? dayjs(restCompte.dateFermeture) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestCompte>): HttpResponse<ICompte> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestCompte[]>): HttpResponse<ICompte[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
