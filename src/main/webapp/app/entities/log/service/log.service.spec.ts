import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ILog } from '../log.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../log.test-samples';

import { LogService, RestLog } from './log.service';

const requireRestSample: RestLog = {
  ...sampleWithRequiredData,
  timestamp: sampleWithRequiredData.timestamp?.toJSON(),
};

describe('Log Service', () => {
  let service: LogService;
  let httpMock: HttpTestingController;
  let expectedResult: ILog | ILog[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(LogService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Log', () => {
      const log = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(log).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Log', () => {
      const log = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(log).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Log', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Log', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Log', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addLogToCollectionIfMissing', () => {
      it('should add a Log to an empty array', () => {
        const log: ILog = sampleWithRequiredData;
        expectedResult = service.addLogToCollectionIfMissing([], log);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(log);
      });

      it('should not add a Log to an array that contains it', () => {
        const log: ILog = sampleWithRequiredData;
        const logCollection: ILog[] = [
          {
            ...log,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addLogToCollectionIfMissing(logCollection, log);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Log to an array that doesn't contain it", () => {
        const log: ILog = sampleWithRequiredData;
        const logCollection: ILog[] = [sampleWithPartialData];
        expectedResult = service.addLogToCollectionIfMissing(logCollection, log);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(log);
      });

      it('should add only unique Log to an array', () => {
        const logArray: ILog[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const logCollection: ILog[] = [sampleWithRequiredData];
        expectedResult = service.addLogToCollectionIfMissing(logCollection, ...logArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const log: ILog = sampleWithRequiredData;
        const log2: ILog = sampleWithPartialData;
        expectedResult = service.addLogToCollectionIfMissing([], log, log2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(log);
        expect(expectedResult).toContain(log2);
      });

      it('should accept null and undefined values', () => {
        const log: ILog = sampleWithRequiredData;
        expectedResult = service.addLogToCollectionIfMissing([], null, log, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(log);
      });

      it('should return initial array if no Log is added', () => {
        const logCollection: ILog[] = [sampleWithRequiredData];
        expectedResult = service.addLogToCollectionIfMissing(logCollection, undefined, null);
        expect(expectedResult).toEqual(logCollection);
      });
    });

    describe('compareLog', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareLog(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 26555 };
        const entity2 = null;

        const compareResult1 = service.compareLog(entity1, entity2);
        const compareResult2 = service.compareLog(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 26555 };
        const entity2 = { id: 22737 };

        const compareResult1 = service.compareLog(entity1, entity2);
        const compareResult2 = service.compareLog(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 26555 };
        const entity2 = { id: 26555 };

        const compareResult1 = service.compareLog(entity1, entity2);
        const compareResult2 = service.compareLog(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
