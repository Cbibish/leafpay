import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IAlerteSecurite } from '../alerte-securite.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../alerte-securite.test-samples';

import { AlerteSecuriteService, RestAlerteSecurite } from './alerte-securite.service';

const requireRestSample: RestAlerteSecurite = {
  ...sampleWithRequiredData,
  timestamp: sampleWithRequiredData.timestamp?.toJSON(),
};

describe('AlerteSecurite Service', () => {
  let service: AlerteSecuriteService;
  let httpMock: HttpTestingController;
  let expectedResult: IAlerteSecurite | IAlerteSecurite[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(AlerteSecuriteService);
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

    it('should create a AlerteSecurite', () => {
      const alerteSecurite = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(alerteSecurite).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AlerteSecurite', () => {
      const alerteSecurite = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(alerteSecurite).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AlerteSecurite', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AlerteSecurite', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a AlerteSecurite', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addAlerteSecuriteToCollectionIfMissing', () => {
      it('should add a AlerteSecurite to an empty array', () => {
        const alerteSecurite: IAlerteSecurite = sampleWithRequiredData;
        expectedResult = service.addAlerteSecuriteToCollectionIfMissing([], alerteSecurite);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(alerteSecurite);
      });

      it('should not add a AlerteSecurite to an array that contains it', () => {
        const alerteSecurite: IAlerteSecurite = sampleWithRequiredData;
        const alerteSecuriteCollection: IAlerteSecurite[] = [
          {
            ...alerteSecurite,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAlerteSecuriteToCollectionIfMissing(alerteSecuriteCollection, alerteSecurite);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AlerteSecurite to an array that doesn't contain it", () => {
        const alerteSecurite: IAlerteSecurite = sampleWithRequiredData;
        const alerteSecuriteCollection: IAlerteSecurite[] = [sampleWithPartialData];
        expectedResult = service.addAlerteSecuriteToCollectionIfMissing(alerteSecuriteCollection, alerteSecurite);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(alerteSecurite);
      });

      it('should add only unique AlerteSecurite to an array', () => {
        const alerteSecuriteArray: IAlerteSecurite[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const alerteSecuriteCollection: IAlerteSecurite[] = [sampleWithRequiredData];
        expectedResult = service.addAlerteSecuriteToCollectionIfMissing(alerteSecuriteCollection, ...alerteSecuriteArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const alerteSecurite: IAlerteSecurite = sampleWithRequiredData;
        const alerteSecurite2: IAlerteSecurite = sampleWithPartialData;
        expectedResult = service.addAlerteSecuriteToCollectionIfMissing([], alerteSecurite, alerteSecurite2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(alerteSecurite);
        expect(expectedResult).toContain(alerteSecurite2);
      });

      it('should accept null and undefined values', () => {
        const alerteSecurite: IAlerteSecurite = sampleWithRequiredData;
        expectedResult = service.addAlerteSecuriteToCollectionIfMissing([], null, alerteSecurite, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(alerteSecurite);
      });

      it('should return initial array if no AlerteSecurite is added', () => {
        const alerteSecuriteCollection: IAlerteSecurite[] = [sampleWithRequiredData];
        expectedResult = service.addAlerteSecuriteToCollectionIfMissing(alerteSecuriteCollection, undefined, null);
        expect(expectedResult).toEqual(alerteSecuriteCollection);
      });
    });

    describe('compareAlerteSecurite', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAlerteSecurite(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 2075 };
        const entity2 = null;

        const compareResult1 = service.compareAlerteSecurite(entity1, entity2);
        const compareResult2 = service.compareAlerteSecurite(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 2075 };
        const entity2 = { id: 1738 };

        const compareResult1 = service.compareAlerteSecurite(entity1, entity2);
        const compareResult2 = service.compareAlerteSecurite(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 2075 };
        const entity2 = { id: 2075 };

        const compareResult1 = service.compareAlerteSecurite(entity1, entity2);
        const compareResult2 = service.compareAlerteSecurite(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
