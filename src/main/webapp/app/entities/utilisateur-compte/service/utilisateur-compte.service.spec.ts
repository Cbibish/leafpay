import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IUtilisateurCompte } from '../utilisateur-compte.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../utilisateur-compte.test-samples';

import { UtilisateurCompteService } from './utilisateur-compte.service';

const requireRestSample: IUtilisateurCompte = {
  ...sampleWithRequiredData,
};

describe('UtilisateurCompte Service', () => {
  let service: UtilisateurCompteService;
  let httpMock: HttpTestingController;
  let expectedResult: IUtilisateurCompte | IUtilisateurCompte[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(UtilisateurCompteService);
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

    it('should create a UtilisateurCompte', () => {
      const utilisateurCompte = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(utilisateurCompte).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a UtilisateurCompte', () => {
      const utilisateurCompte = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(utilisateurCompte).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a UtilisateurCompte', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of UtilisateurCompte', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a UtilisateurCompte', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addUtilisateurCompteToCollectionIfMissing', () => {
      it('should add a UtilisateurCompte to an empty array', () => {
        const utilisateurCompte: IUtilisateurCompte = sampleWithRequiredData;
        expectedResult = service.addUtilisateurCompteToCollectionIfMissing([], utilisateurCompte);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(utilisateurCompte);
      });

      it('should not add a UtilisateurCompte to an array that contains it', () => {
        const utilisateurCompte: IUtilisateurCompte = sampleWithRequiredData;
        const utilisateurCompteCollection: IUtilisateurCompte[] = [
          {
            ...utilisateurCompte,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addUtilisateurCompteToCollectionIfMissing(utilisateurCompteCollection, utilisateurCompte);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a UtilisateurCompte to an array that doesn't contain it", () => {
        const utilisateurCompte: IUtilisateurCompte = sampleWithRequiredData;
        const utilisateurCompteCollection: IUtilisateurCompte[] = [sampleWithPartialData];
        expectedResult = service.addUtilisateurCompteToCollectionIfMissing(utilisateurCompteCollection, utilisateurCompte);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(utilisateurCompte);
      });

      it('should add only unique UtilisateurCompte to an array', () => {
        const utilisateurCompteArray: IUtilisateurCompte[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const utilisateurCompteCollection: IUtilisateurCompte[] = [sampleWithRequiredData];
        expectedResult = service.addUtilisateurCompteToCollectionIfMissing(utilisateurCompteCollection, ...utilisateurCompteArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const utilisateurCompte: IUtilisateurCompte = sampleWithRequiredData;
        const utilisateurCompte2: IUtilisateurCompte = sampleWithPartialData;
        expectedResult = service.addUtilisateurCompteToCollectionIfMissing([], utilisateurCompte, utilisateurCompte2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(utilisateurCompte);
        expect(expectedResult).toContain(utilisateurCompte2);
      });

      it('should accept null and undefined values', () => {
        const utilisateurCompte: IUtilisateurCompte = sampleWithRequiredData;
        expectedResult = service.addUtilisateurCompteToCollectionIfMissing([], null, utilisateurCompte, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(utilisateurCompte);
      });

      it('should return initial array if no UtilisateurCompte is added', () => {
        const utilisateurCompteCollection: IUtilisateurCompte[] = [sampleWithRequiredData];
        expectedResult = service.addUtilisateurCompteToCollectionIfMissing(utilisateurCompteCollection, undefined, null);
        expect(expectedResult).toEqual(utilisateurCompteCollection);
      });
    });

    describe('compareUtilisateurCompte', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareUtilisateurCompte(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 7671 };
        const entity2 = null;

        const compareResult1 = service.compareUtilisateurCompte(entity1, entity2);
        const compareResult2 = service.compareUtilisateurCompte(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 7671 };
        const entity2 = { id: 30926 };

        const compareResult1 = service.compareUtilisateurCompte(entity1, entity2);
        const compareResult2 = service.compareUtilisateurCompte(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 7671 };
        const entity2 = { id: 7671 };

        const compareResult1 = service.compareUtilisateurCompte(entity1, entity2);
        const compareResult2 = service.compareUtilisateurCompte(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
