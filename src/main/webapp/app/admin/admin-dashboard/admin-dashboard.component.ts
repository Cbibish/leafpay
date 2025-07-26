import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdminDashboardService } from './admin-dashboard.service';

@Component({
  standalone: true,
  selector: 'app-admin',
  imports: [CommonModule],
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.scss'],
})
export class AdminComponent implements OnInit {
  activeTab: string = 'users';
  users: any[] = [];
  transactions: any[] = [];
  logs: string[] = [];

  constructor(private adminDashboardService: AdminDashboardService) {}

  ngOnInit(): void {
    this.loadUsers();
    this.loadTransactions();
    this.loadLogs();
  }

  setTab(tab: string): void {
    this.activeTab = tab;
  }

  loadUsers(): void {
    this.adminDashboardService.getUsers().subscribe(data => (this.users = data));
  }

  loadTransactions(): void {
    this.adminDashboardService.getTransactions().subscribe(data => (this.transactions = data));
  }

  loadLogs(): void {
    this.adminDashboardService.getLogs().subscribe(data => (this.logs = data));
  }

  createUser(): void {
    // Show modal or navigate to form
  }

  editUser(user: any): void {
    // Show modal or navigate to edit form
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
}
