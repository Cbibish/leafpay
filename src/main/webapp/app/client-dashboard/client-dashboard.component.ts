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

interface CompteDTO {
  compteId: number;
  iban: string;
  typeCompte: string;
  solde: number;
  utilisateurId: number;
  roleUtilisateurSurCeCompte?: string;
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
  comptes: CompteDTO[] = [];
  loading = true;
  transactions: Transaction[] = [];
  selectedCompteId?: number;
  isProfessional = false;
  currentUserId!: number;

  amount: number | null = null;
  operationType: 'depot' | 'retrait' = 'depot';
  moneyOperationMessage = '';
  moneyOperationSuccess = false;

  showTransferForm = false;
  ibanValid = false;
  ibanCheckMessage: string = '';
  showLinkModal = false;

  transferForm = {
    fromAccountId: null as number | null,
    toIban: '',
    toAccount: null as CompteDTO | null,
    amount: null as number | null,
    justificatif: 'Invoice #7890',
    moyenValidation: 'SMS Code',
  };

  transferLoading = false;
  transferMessage = '';
  transferSuccess = false;

  linkIban: string = '';
  linkMessage: string = '';
  linkSuccess: boolean = false;

  constructor(
    private accountService: AccountService,
    private http: HttpClient,
  ) {}

  ngOnInit(): void {
    this.accountService.identity().subscribe(account => {
      if (account && account.id) {
        this.isProfessional = account.authorities.includes('ROLE_PROFESSIONAL_USER');
        this.currentUserId = account.id;
        this.loadComptes(account.id);
      } else {
        this.comptes = [];
        this.loading = false;
        this.selectedCompteId = undefined;
        this.transactions = [];
      }
    });
  }

  linkProfessionalAccount(): void {
    if (!this.linkIban || this.linkIban.trim().length < 10) {
      this.linkMessage = 'IBAN invalide.';
      this.linkSuccess = false;
      return;
    }

    const payload = {
      iban: this.linkIban.trim(),
      userId: this.currentUserId,
    };

    this.http.post<void>('/api/comptes/link-by-iban', payload).subscribe({
      next: () => {
        this.linkMessage = 'Compte lié avec succès !';
        this.linkSuccess = true;
        this.linkIban = '';
        this.loadComptes(this.currentUserId);
      },
      error: err => {
        this.linkMessage = err?.error?.message || 'Erreur lors du lien de compte.';
        this.linkSuccess = false;
      },
    });
  }

  loadComptes(userId: number): void {
    this.loading = true;
    this.http.get<CompteDTO[]>(`/api/utilisateur-comptes/utilisateur/${userId}/details`).subscribe({
      next: comptes => {
        this.comptes = comptes;
        this.loading = false;

        if (comptes.length > 0) {
          this.selectedCompteId = comptes[0].compteId;
          this.transferForm.fromAccountId = this.selectedCompteId;
          this.loadTransactions(this.selectedCompteId);
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

    if (this.showTransferForm) {
      this.transferForm.fromAccountId = newCompteId;
    }
  }

  selectTransferFromAccount(account: CompteDTO) {
    this.transferForm.fromAccountId = account.compteId;
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

    const request$ =
      this.operationType === 'depot'
        ? this.http.post('/api/transactions/deposit', payload)
        : this.http.post('/api/transactions/withdraw', payload);

    request$.subscribe({
      next: () => {
        this.moneyOperationMessage = `Opération ${this.operationType === 'depot' ? 'de dépôt' : 'de retrait'} réussie !`;
        this.moneyOperationSuccess = true;
        if (this.selectedCompteId != null) {
          this.loadTransactions(this.selectedCompteId);
        }
        this.amount = null;
        this.operationType = 'depot';
      },
      error: err => {
        this.handleMoneyError(err);
      },
    });
  }

  private handleMoneyError(err: any) {
    this.moneyOperationSuccess = false;
    this.moneyOperationMessage =
      err?.error?.title === 'Insufficient balance' ? 'Solde insuffisant' : err?.error?.message || "Erreur lors de l'opération";
  }

  openTransferForm() {
    this.transferMessage = '';
    this.transferSuccess = false;
    this.showTransferForm = true;

    this.transferForm = {
      fromAccountId: this.selectedCompteId ?? (this.comptes.length > 0 ? this.comptes[0].compteId : null),
      toIban: '',
      toAccount: null,
      amount: null,
      justificatif: 'Invoice #7890',
      moyenValidation: 'SMS Code',
    };
  }

  closeTransferForm() {
    this.showTransferForm = false;
  }

  isTransferFormValid(): boolean {
    const f = this.transferForm;
    return (
      f.fromAccountId != null &&
      f.toAccount != null &&
      f.amount != null &&
      f.amount > 0 &&
      f.justificatif.trim().length > 0 &&
      f.moyenValidation.trim().length > 0
    );
  }

  checkIbanExists(iban: string): void {
    if (!iban || iban.trim().length < 10) {
      this.ibanValid = false;
      this.ibanCheckMessage = 'IBAN trop court ou invalide.';
      this.transferForm.toAccount = null;
      return;
    }

    this.http.get<CompteDTO>(`/api/comptes/by-iban?iban=${encodeURIComponent(iban.trim())}`).subscribe({
      next: compte => {
        this.ibanValid = true;
        this.transferForm.toAccount = compte;
        this.ibanCheckMessage = '';
      },
      error: () => {
        this.ibanValid = false;
        this.transferForm.toAccount = null;
        this.ibanCheckMessage = 'IBAN introuvable.';
      },
    });
  }

  submitTransfer(): void {
    const f = this.transferForm;

    if (
      f.fromAccountId == null ||
      f.toAccount == null ||
      f.amount == null ||
      f.amount <= 0 ||
      !f.justificatif?.trim() ||
      !f.moyenValidation?.trim()
    ) {
      this.transferMessage = 'Veuillez remplir correctement tous les champs et vérifier l’IBAN.';
      this.transferSuccess = false;
      return;
    }

    const fromAccount = this.comptes.find(c => c.compteId === f.fromAccountId);
    if (!fromAccount || !fromAccount.iban) {
      this.transferMessage = "IBAN de l'expéditeur introuvable.";
      this.transferSuccess = false;
      return;
    }

    const payload = {
      fromIban: fromAccount.iban,
      toIban: f.toAccount.iban,
      amount: f.amount,
      justificatif: f.justificatif.trim(),
      moyenValidation: f.moyenValidation.trim(),
    };

    this.transferLoading = true;
    this.http.post('/api/comptes/transfer-by-iban', payload).subscribe({
      next: () => {
        this.transferMessage = 'Virement effectué avec succès !';
        this.transferSuccess = true;

        if (f.fromAccountId != null) {
          this.loadTransactions(f.fromAccountId);
        }
        this.closeTransferForm();
        this.transferLoading = false;
      },
      error: err => {
        this.transferMessage =
          err?.error?.title === 'Insufficient balance' ? 'Solde insuffisant' : err?.error?.message || 'Erreur lors du virement';
        this.transferSuccess = false;
        this.transferLoading = false;
      },
    });
  }

  refaireVirement(transaction: Transaction) {
    this.transferForm = {
      fromAccountId: transaction.compteSource.id,
      toIban: transaction.compteDestination?.iban || '',
      toAccount: null,
      amount: transaction.montant,
      justificatif: 'Invoice #7890',
      moyenValidation: 'SMS Code',
    };
    this.openTransferForm();
    if (this.transferForm.toIban) {
      this.checkIbanExists(this.transferForm.toIban);
    }
  }

  onFromAccountChange(value: string) {
    this.transferForm.fromAccountId = Number(value);
  }

  openLinkModal(): void {
    this.linkMessage = '';
    this.linkSuccess = false;
    this.linkIban = '';
    this.showLinkModal = true;
  }

  closeLinkModal(): void {
    this.showLinkModal = false;
  }

  submitLinkAccount(): void {
    if (!this.linkIban || this.linkIban.trim().length < 10) {
      this.linkMessage = 'IBAN invalide.';
      this.linkSuccess = false;
      return;
    }

    const payload = {
      iban: this.linkIban.trim(),
      userId: this.currentUserId,
    };

    this.http.post<void>('/api/comptes/link-by-iban', payload).subscribe({
      next: () => {
        this.linkMessage = 'Compte lié avec succès !';
        this.linkSuccess = true;
        this.loadComptes(this.currentUserId);
        this.linkIban = '';
      },
      error: err => {
        this.linkMessage = err?.error?.message || 'Erreur lors du lien de compte.';
        this.linkSuccess = false;
      },
    });
  }
}
