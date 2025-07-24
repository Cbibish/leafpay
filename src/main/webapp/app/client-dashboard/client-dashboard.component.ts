import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-client-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './client-dashboard.component.html',
  styleUrl: './client-dashboard.component.scss',
})
export class ClientDashboardComponent {
  activeTab: 'account' | 'money' | 'transfer' = 'account';

  setTab(tab: 'account' | 'money' | 'transfer'): void {
    this.activeTab = tab;
  }
}
