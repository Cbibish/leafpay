import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AdminDashboardService } from './admin-dashboard.service';
import { AccountService } from 'app/core/auth/account.service';

interface Log {
  id: number;
  action: string;
  timestamp: string;
  ipUtilisateur: string;
  resultat: string;
  description: string;
}

@Component({
  standalone: true,
  selector: 'app-admin',
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.scss'],
})
export class AdminComponent implements OnInit {
  activeTab = 'users';
  users: any[] = [];
  transactions: any[] = [];
  logs: Log[] = [];
  filteredLogs: Log[] = [];
  filterType = '';
  accounts: any[] = [];
  filteredAccounts: any[] = [];
  ibanSearch = '';
  userAuthorities: string[] = [];
  editingUser: any = null;
  selectedRoleId = 0;

  availableRoles = [
    { id: 1501, nom: 'NORMAL_USER' },
    { id: 1502, nom: 'PROFESSIONAL_USER' },
    { id: 1503, nom: 'ADMIN' },
    { id: 1504, nom: 'CONSEILLER' },
  ];

  constructor(
    private adminDashboardService: AdminDashboardService,
    private accountService: AccountService,
  ) {}

  ngOnInit(): void {
    this.accountService.identity().subscribe(account => {
      this.userAuthorities = account?.authorities ?? [];

      // Set default tab based on role
      if (this.hasRole('ROLE_ADMIN')) {
        this.activeTab = 'users';
      } else if (this.hasRole('ROLE_CONSEILLER')) {
        this.activeTab = 'transactions';
      }
    });

    this.loadUsers();
    this.loadTransactions();
    this.loadLogs();
    this.loadAccounts();
    this.filteredAccounts = [...this.accounts];
  }

  hasRole(role: string): boolean {
    return this.userAuthorities.includes(role);
  }

  setTab(tab: string): void {
    // If conseiller tries to access admin-only tabs, ignore
    if ((tab === 'users' || tab === 'logs') && !this.hasRole('ROLE_ADMIN')) {
      return;
    }
    this.activeTab = tab;
  }
  loadUsers(): void {
    this.adminDashboardService.getUsers().subscribe(data => (this.users = data));
  }

  loadTransactions(): void {
    this.adminDashboardService.getTransactions().subscribe(data => (this.transactions = data));
  }

  loadLogs(): void {
    this.adminDashboardService.getLogs().subscribe(data => {
      this.logs = data;
      this.applyFilter();
    });
  }

  applyFilter(): void {
    this.filteredLogs = this.filterType ? this.logs.filter(log => log.action === this.filterType) : this.logs;
  }

  onFilterChange(event: Event): void {
    const target = event.target as HTMLSelectElement | null;
    if (target) {
      this.filterType = target.value;
      this.applyFilter();
    }
  }

  createUser(): void {
    // Add user modal logic here
  }

  editUser(user: any): void {
    this.editingUser = { ...user };
    this.selectedRoleId = user.role?.id || 0;
  }

  closeModal(): void {
    this.editingUser = null;
    this.selectedRoleId = 0;
  }

  updateUserRole(): void {
    if (!this.editingUser) return;

    const updatedUser = {
      ...this.editingUser,
      role: { id: this.selectedRoleId },
    };

    this.adminDashboardService.updateUser(updatedUser.id, updatedUser).subscribe(() => {
      this.closeModal();
      this.loadUsers();
    });
  }

  deleteUser(id: number): void {
    this.adminDashboardService.deleteUser(id).subscribe(() => this.loadUsers());
  }

  approveTransaction(id: number): void {
    this.adminDashboardService.approveTransaction(id).subscribe(() => this.loadTransactions());
  }

  rejectTransaction(id: number): void {
    this.adminDashboardService.rejectTransaction(id).subscribe(() => this.loadTransactions());
  }
  filterAccounts(): void {
    const search = this.ibanSearch.trim().toLowerCase();

    if (!search) {
      // If search is empty, show all accounts
      this.filteredAccounts = [...this.accounts];
    } else {
      this.filteredAccounts = this.accounts.filter(account => account.iban.toLowerCase().includes(search));

      if (this.filteredAccounts.length === 0) {
        alert(`Aucun compte trouvé pour l’IBAN "${this.ibanSearch}".`);
      }
    }
  }

  closeAccount(id: number): void {
    this.adminDashboardService.deactivateAccount(id, { dateFermeture: new Date().toISOString() }).subscribe(updatedAccount => {
      // Update the account in local list
      const index = this.accounts.findIndex(acc => acc.id === id);
      if (index !== -1) {
        this.accounts[index] = updatedAccount;
        this.filterAccounts();
      }
    });
  }

  loadAccounts(): void {
    this.adminDashboardService.getActiveAccounts().subscribe(data => {
      this.accounts = data;
      this.filteredAccounts = [...this.accounts];
    });
  }
}
