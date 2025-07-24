import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAlerteSecurite, NewAlerteSecurite } from '../alerte-securite.model';

export type PartialUpdateAlerteSecurite = Partial<IAlerteSecurite> & Pick<IAlerteSecurite, 'id'>;

type RestOf<T extends IAlerteSecurite | NewAlerteSecurite> = Omit<T, 'timestamp'> & {
  timestamp?: string | null;
};

export type RestAlerteSecurite = RestOf<IAlerteSecurite>;

export type NewRestAlerteSecurite = RestOf<NewAlerteSecurite>;

export type PartialUpdateRestAlerteSecurite = RestOf<PartialUpdateAlerteSecurite>;

export type EntityResponseType = HttpResponse<IAlerteSecurite>;
export type EntityArrayResponseType = HttpResponse<IAlerteSecurite[]>;

@Injectable({ providedIn: 'root' })
export class AlerteSecuriteService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/alerte-securites');

  create(alerteSecurite: NewAlerteSecurite): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(alerteSecurite);
    return this.http
      .post<RestAlerteSecurite>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(alerteSecurite: IAlerteSecurite): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(alerteSecurite);
    return this.http
      .put<RestAlerteSecurite>(`${this.resourceUrl}/${this.getAlerteSecuriteIdentifier(alerteSecurite)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(alerteSecurite: PartialUpdateAlerteSecurite): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(alerteSecurite);
    return this.http
      .patch<RestAlerteSecurite>(`${this.resourceUrl}/${this.getAlerteSecuriteIdentifier(alerteSecurite)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestAlerteSecurite>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestAlerteSecurite[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAlerteSecuriteIdentifier(alerteSecurite: Pick<IAlerteSecurite, 'id'>): number {
    return alerteSecurite.id;
  }

  compareAlerteSecurite(o1: Pick<IAlerteSecurite, 'id'> | null, o2: Pick<IAlerteSecurite, 'id'> | null): boolean {
    return o1 && o2 ? this.getAlerteSecuriteIdentifier(o1) === this.getAlerteSecuriteIdentifier(o2) : o1 === o2;
  }

  addAlerteSecuriteToCollectionIfMissing<Type extends Pick<IAlerteSecurite, 'id'>>(
    alerteSecuriteCollection: Type[],
    ...alerteSecuritesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const alerteSecurites: Type[] = alerteSecuritesToCheck.filter(isPresent);
    if (alerteSecurites.length > 0) {
      const alerteSecuriteCollectionIdentifiers = alerteSecuriteCollection.map(alerteSecuriteItem =>
        this.getAlerteSecuriteIdentifier(alerteSecuriteItem),
      );
      const alerteSecuritesToAdd = alerteSecurites.filter(alerteSecuriteItem => {
        const alerteSecuriteIdentifier = this.getAlerteSecuriteIdentifier(alerteSecuriteItem);
        if (alerteSecuriteCollectionIdentifiers.includes(alerteSecuriteIdentifier)) {
          return false;
        }
        alerteSecuriteCollectionIdentifiers.push(alerteSecuriteIdentifier);
        return true;
      });
      return [...alerteSecuritesToAdd, ...alerteSecuriteCollection];
    }
    return alerteSecuriteCollection;
  }

  protected convertDateFromClient<T extends IAlerteSecurite | NewAlerteSecurite | PartialUpdateAlerteSecurite>(
    alerteSecurite: T,
  ): RestOf<T> {
    return {
      ...alerteSecurite,
      timestamp: alerteSecurite.timestamp?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restAlerteSecurite: RestAlerteSecurite): IAlerteSecurite {
    return {
      ...restAlerteSecurite,
      timestamp: restAlerteSecurite.timestamp ? dayjs(restAlerteSecurite.timestamp) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestAlerteSecurite>): HttpResponse<IAlerteSecurite> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestAlerteSecurite[]>): HttpResponse<IAlerteSecurite[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
