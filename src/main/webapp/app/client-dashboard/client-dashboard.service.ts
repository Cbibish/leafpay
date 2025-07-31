import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class ClientDashboardService {
  private apiUrl = 'http://localhost:9000/api';

  constructor(private http: HttpClient) {}

  // Get logged-in user info
  getAccount(): Observable<any> {
    const token = localStorage.getItem('token'); // or from your AuthService
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });

    return this.http.get(`${this.apiUrl}/api/account`, { headers });
  }

  // Get all accounts for the user using their ID
  getUserAccounts(utilisateurId: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/utilisateur-comptes/utilisateur/${utilisateurId}/details`);
  }
}
