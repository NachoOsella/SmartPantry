import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'sp-card',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="card" [class.no-padding]="noPadding">
      <div class="card-header" *ngIf="title">
        <h3>{{ title }}</h3>
        <ng-content select="[header-actions]"></ng-content>
      </div>
      <div class="card-content">
        <ng-content></ng-content>
      </div>
    </div>
  `,
  styles: [`
    .card {
      background: white;
      border: 1px solid var(--border-color);
      border-radius: 8px;
      overflow: hidden;
    }

    .card-header {
      padding: var(--spacing-md) var(--spacing-lg);
      border-bottom: 1px solid var(--border-color);
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    .card-header h3 {
      font-size: 1rem;
      font-weight: 600;
      color: var(--text-main);
    }

    .card-content {
      padding: var(--spacing-lg);
    }

    .no-padding .card-content {
      padding: 0;
    }
  `]
})
export class CardComponent {
  @Input() title = '';
  @Input() noPadding = false;
}
