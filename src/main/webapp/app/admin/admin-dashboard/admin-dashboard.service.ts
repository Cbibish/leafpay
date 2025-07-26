import { Injectable } from '@angular/core';
import { Observable, forkJoin } from 'rxjs';
import { UtilisateurService } from 'app/entities/utilisateur/service/utilisateur.service';
import { TransactionService } from 'app/entities/transaction/service/transaction.service';
import { LogService } from 'app/entities/log/service/log.service';

@Injectable({ providedIn: 'root' })
export class AdminDashboardService {
  constructor(
    private utilisateurService: UtilisateurService,
    private transactionService: TransactionService,
    private logService: LogService,
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
  getUsers(): Observable<any> {
    return this.utilisateurService.query();
  }

  getTransactions(): Observable<any> {
    return this.transactionService.query();
  }

  getLogs(): Observable<any> {
    return this.logService.query();
  }

  // CRUD & Approvals
  deleteUser(id: number): Observable<any> {
    return this.utilisateurService.delete(id);
  }

  approveTransaction(id: number): Observable<any> {
    return this.transactionService.update({ id, statut: 'APPROVED' });
  }

  rejectTransaction(id: number): Observable<any> {
    return this.transactionService.update({ id, statut: 'REJECTED' });
  }
}
