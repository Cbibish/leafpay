import { Injectable } from '@angular/core';
import { Observable, forkJoin } from 'rxjs';
import { UtilisateurService } from 'app/entities/utilisateur/service/utilisateur.service';
import { TransactionService } from 'app/entities/transaction/service/transaction.service';
import { LogService } from 'app/entities/log/service/log.service';
import { HttpClient } from '@angular/common/http';

@Injectable({ providedIn: 'root' })
export class AdminDashboardService {
  constructor(
    private utilisateurService: UtilisateurService,
    private transactionService: TransactionService,
    private logService: LogService,
    private http: HttpClient,
  ) {}

  // For full dashboard data
  loadDashboardData(): Observable<any> {
    return forkJoin({
      users: this.utilisateurService.query(),
      transactions: this.transactionService.query(),
      logs: this.logService.query(),
    });
  }

  // Individual calls
  getUsers(): Observable<any[]> {
    return this.http.get<any[]>('/api/utilisateurs');
  }

  getTransactions(): Observable<any[]> {
    return this.http.get<any[]>(`/api/transactions/important`);
  }

  getLogs(): Observable<any[]> {
    return this.http.get<any[]>('/api/logs');
  }

  // CRUD & Approvals
  deleteUser(id: number): Observable<any> {
    return this.utilisateurService.delete(id);
  }

  approveTransaction(id: number): Observable<any> {
    return this.http.put(`/api/transactions/${id}/validate`, {});
  }

  updateUser(id: number, userData: any): Observable<any> {
    return this.http.put(`/api/utilisateurs/${id}`, userData);
  }

  rejectTransaction(id: number): Observable<any> {
    return this.http.put(`/api/transactions/${id}/reject`, {});
  }
}
