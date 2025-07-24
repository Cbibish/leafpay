import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILog, NewLog } from '../log.model';

export type PartialUpdateLog = Partial<ILog> & Pick<ILog, 'id'>;

type RestOf<T extends ILog | NewLog> = Omit<T, 'timestamp'> & {
  timestamp?: string | null;
};

export type RestLog = RestOf<ILog>;

export type NewRestLog = RestOf<NewLog>;

export type PartialUpdateRestLog = RestOf<PartialUpdateLog>;

export type EntityResponseType = HttpResponse<ILog>;
export type EntityArrayResponseType = HttpResponse<ILog[]>;

@Injectable({ providedIn: 'root' })
export class LogService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/logs');

  create(log: NewLog): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(log);
    return this.http.post<RestLog>(this.resourceUrl, copy, { observe: 'response' }).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(log: ILog): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(log);
    return this.http
      .put<RestLog>(`${this.resourceUrl}/${this.getLogIdentifier(log)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(log: PartialUpdateLog): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(log);
    return this.http
      .patch<RestLog>(`${this.resourceUrl}/${this.getLogIdentifier(log)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestLog>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestLog[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getLogIdentifier(log: Pick<ILog, 'id'>): number {
    return log.id;
  }

  compareLog(o1: Pick<ILog, 'id'> | null, o2: Pick<ILog, 'id'> | null): boolean {
    return o1 && o2 ? this.getLogIdentifier(o1) === this.getLogIdentifier(o2) : o1 === o2;
  }

  addLogToCollectionIfMissing<Type extends Pick<ILog, 'id'>>(logCollection: Type[], ...logsToCheck: (Type | null | undefined)[]): Type[] {
    const logs: Type[] = logsToCheck.filter(isPresent);
    if (logs.length > 0) {
      const logCollectionIdentifiers = logCollection.map(logItem => this.getLogIdentifier(logItem));
      const logsToAdd = logs.filter(logItem => {
        const logIdentifier = this.getLogIdentifier(logItem);
        if (logCollectionIdentifiers.includes(logIdentifier)) {
          return false;
        }
        logCollectionIdentifiers.push(logIdentifier);
        return true;
      });
      return [...logsToAdd, ...logCollection];
    }
    return logCollection;
  }

  protected convertDateFromClient<T extends ILog | NewLog | PartialUpdateLog>(log: T): RestOf<T> {
    return {
      ...log,
      timestamp: log.timestamp?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restLog: RestLog): ILog {
    return {
      ...restLog,
      timestamp: restLog.timestamp ? dayjs(restLog.timestamp) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestLog>): HttpResponse<ILog> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestLog[]>): HttpResponse<ILog[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
