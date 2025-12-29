import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../../core/services/auth.service';
import { ButtonComponent } from '../../../shared/components/button/button.component';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, ButtonComponent],
  template: `
    <div class="dashboard-layout">
      <nav class="navbar">
        <div class="brand">SmartPantry</div>
        <div class="user-info">
          <span>Welcome, {{ authService.currentUser()?.username }}</span>
          <sp-button variant="ghost" (click)="authService.logout()">Logout</sp-button>
        </div>
      </nav>
      
      <main class="content">
        <header class="content-header">
          <h1>Inventory Overview</h1>
          <p>You are logged in and your session is secure.</p>
        </header>
      </main>
    </div>
  `,
  styles: [`
    .dashboard-layout {
      min-height: 100vh;
      display: flex;
      flex-direction: column;
    }

    .navbar {
      height: 64px;
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 0 var(--spacing-xl);
      background: white;
      border-bottom: 1px solid var(--border-color);
    }

    .brand {
      font-weight: 700;
      color: var(--primary);
      font-size: 1.25rem;
    }

    .user-info {
      display: flex;
      align-items: center;
      gap: var(--spacing-md);
      font-size: 0.875rem;
    }

    .content {
      flex: 1;
      padding: var(--spacing-xl);
      background-color: var(--bg-surface);
    }

    .content-header h1 {
      font-size: 1.5rem;
      font-weight: 700;
      margin-bottom: 0.25rem;
    }

    .content-header p {
      color: var(--text-muted);
      font-size: 0.875rem;
    }
  `]
})
export class DashboardComponent {
  constructor(public authService: AuthService) {}
}
