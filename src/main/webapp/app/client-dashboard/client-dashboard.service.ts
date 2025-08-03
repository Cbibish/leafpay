import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CompteDTO } from 'app/admin/admin-dashboard/admin-dashboard.service';

@Injectable({ providedIn: 'root' })
export class ClientDashboardService {
  private apiUrl = 'http://localhost:9000/api';

  constructor(private http: HttpClient) {}

  getAccount(): Observable<any> {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });

    return this.http.get(`${this.apiUrl}/account`, { headers });
  }

  getUserAccounts(utilisateurId: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/utilisateur-comptes/utilisateur/${utilisateurId}/details`);
  }

  getAccountByIban(iban: string): Observable<CompteDTO> {
    return this.http.get<CompteDTO>(`${this.apiUrl}/comptes/by-iban?iban=${encodeURIComponent(iban)}`);
  }

  performTransfer(payload: {
    fromAccountId: number;
    toAccountId: number;
    amount: number;
    justificatif: string;
    moyenValidation: string;
  }): Observable<any> {
    return this.http.post(`${this.apiUrl}/transactions/transfer`, payload);
  }
}
