import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { AccountService } from 'app/core/auth/account.service';
import { FormsModule } from '@angular/forms';

interface Transaction {
  id: number;
  montant: number;
  typeTransaction: string;
  dateTransaction: string;
  statut: string;
  compteSource: { id: number; iban?: string };
  compteDestination?: { id: number; iban?: string };
}

@Component({
  selector: 'app-client-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './client-dashboard.component.html',
  styleUrls: ['./client-dashboard.component.scss'],
})
export class ClientDashboardComponent implements OnInit {
  activeTab: 'account' | 'money' | 'transfer' = 'account';
  comptes: any[] = [];
  loading = true;
  transactions: Transaction[] = [];
  selectedCompteId?: number;

  // Deposit / Withdraw
  amount: number | null = null;
  operationType: 'depot' | 'retrait' = 'depot';
  moneyOperationMessage = '';
  moneyOperationSuccess = false;

  constructor(
    private accountService: AccountService,
    private http: HttpClient,
  ) {}

  ngOnInit(): void {
    this.accountService.getAuthenticationState().subscribe(account => {
      if (account && account.id) {
        this.loadComptes(account.id);
      } else {
        this.comptes = [];
        this.loading = false;
        this.selectedCompteId = undefined;
        this.transactions = [];
      }
    });
  }

  loadComptes(userId: number): void {
    this.loading = true;
    this.http.get<any[]>(`/api/utilisateur-comptes/utilisateur/${userId}/details`).subscribe({
      next: comptes => {
        this.comptes = comptes;
        this.loading = false;
        if (comptes.length > 0) {
          this.selectedCompteId = comptes[0].compteId;
          if (this.selectedCompteId != null) {
            this.loadTransactions(this.selectedCompteId);
          }
        } else {
          this.selectedCompteId = undefined;
          this.transactions = [];
        }
      },
      error: () => {
        this.comptes = [];
        this.loading = false;
        this.selectedCompteId = undefined;
        this.transactions = [];
      },
    });
  }

  setTab(tab: 'account' | 'money' | 'transfer'): void {
    this.activeTab = tab;
  }

  loadTransactions(compteId: number): void {
    this.http.get<Transaction[]>(`/api/transactions/account/${compteId}`).subscribe({
      next: transactions => {
        this.transactions = transactions;
      },
      error: () => {
        this.transactions = [];
      },
    });
  }

  onAccountChange(event: Event): void {
    const selectElement = event.target as HTMLSelectElement;
    const newCompteId = Number(selectElement.value);
    this.selectedCompteId = newCompteId;
    this.loadTransactions(newCompteId);
  }

  get transferTransactions(): Transaction[] {
    return this.transactions.filter(t => t.typeTransaction === 'TRANSFER');
  }

  isMoneyFormValid(): boolean {
    return this.amount !== null && this.amount > 0 && this.selectedCompteId != null;
  }

  submitMoneyOperation(): void {
    if (!this.isMoneyFormValid()) {
      this.moneyOperationMessage = 'Veuillez saisir un montant valide et sélectionner un compte.';
      this.moneyOperationSuccess = false;
      return;
    }

    const payload = {
      compteId: this.selectedCompteId,
      montant: this.amount,
    };

    let request$;

    if (this.operationType === 'depot') {
      request$ = this.http.post('/api/transactions/deposit', payload);
    } else {
      request$ = this.http.post('/api/transactions/withdraw', payload);
    }

    request$.subscribe({
      next: () => {
        this.moneyOperationMessage = `Opération ${this.operationType === 'depot' ? 'de dépôt' : 'de retrait'} réussie !`;
        this.moneyOperationSuccess = true;

        // Refresh comptes and transactions to show updated balance and history
        if (this.selectedCompteId != null) {
          const userId = this.comptes.length > 0 ? this.comptes[0].utilisateurId : undefined;
          if (userId) {
            this.loadComptes(userId);
          }
          this.loadTransactions(this.selectedCompteId);
        }

        // reset form
        this.amount = null;
        this.operationType = 'depot';
      },
      error: err => {
        if (err?.error) {
          const error = err.error;

          // Handle Insufficient Balance error
          if (error.title == 'Insufficient balance') {
            this.moneyOperationMessage = 'Not enough balance';
            this.moneyOperationSuccess = false;
            return;
          }

          // Handle Withdrawal Attempt Limit Exceeded
          if (error.title === 'Withdrawal attempt limit reached' || error.message === 'error.withdrawalLimitExceeded') {
            this.moneyOperationMessage = 'You have reached the maximum number of withdrawals allowed today for this account.';
            this.moneyOperationSuccess = false;
            return;
          }

          // ... your existing error parsing here ...
          if (typeof error === 'string') {
            this.moneyOperationMessage = error;
          } else if (typeof error === 'object') {
            if ('text' in error && typeof error.text === 'string') {
              this.moneyOperationMessage = error.text;
              this.moneyOperationSuccess = true;
            } else if ('message' in error && typeof error.message === 'string') {
              this.moneyOperationMessage = error.message;
            } else if (Object.keys(error).length === 0) {
              this.moneyOperationMessage = 'Unknown error occurred';
            } else {
              this.moneyOperationMessage = JSON.stringify(error);
            }
          } else {
            this.moneyOperationMessage = 'An error occurred during the operation.';
          }
        } else {
          this.moneyOperationMessage = 'An error occurred during the operation.';
        }
        this.moneyOperationSuccess =
          this.moneyOperationMessage.toLowerCase().includes('success') || this.moneyOperationMessage.toLowerCase().includes('réussi');
      },
    });
  }
}
