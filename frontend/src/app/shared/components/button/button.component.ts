import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'sp-button',
  standalone: true,
  imports: [CommonModule],
  template: `
    <button [type]="type" 
            [class]="'btn ' + variant" 
            [disabled]="disabled"
            (click)="onClick.emit($event)">
      <ng-content></ng-content>
    </button>
  `,
  styles: [`
    .btn {
      padding: 0.625rem 1.25rem;
      border-radius: 4px;
      font-weight: 500;
      font-size: 0.875rem;
      transition: all 0.2s ease;
      display: inline-flex;
      align-items: center;
      justify-content: center;
      gap: 0.5rem;
    }

    .primary {
      background-color: var(--primary);
      color: var(--text-inverse);
    }

    .primary:hover:not(:disabled) {
      background-color: var(--primary-hover);
    }

    .secondary {
      background-color: var(--primary-light);
      color: var(--primary);
    }

    .ghost {
      background-color: transparent;
      color: var(--text-muted);
      border: 1px solid var(--border-color);
    }

    .ghost:hover:not(:disabled) {
      background-color: var(--bg-surface);
      border-color: var(--primary);
    }

    .btn:disabled {
      opacity: 0.5;
      cursor: not-allowed;
    }
  `]
})
export class ButtonComponent {
  @Input() type: 'button' | 'submit' = 'button';
  @Input() variant: 'primary' | 'secondary' | 'ghost' = 'primary';
  @Input() disabled = false;
  @Input() onClick = { emit: (event: any) => {} }; // Simplified for now
}
