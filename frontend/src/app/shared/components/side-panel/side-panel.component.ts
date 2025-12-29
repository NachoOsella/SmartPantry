import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'sp-side-panel',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="overlay" *ngIf="isOpen" (click)="close()"></div>
    <div class="panel" [class.open]="isOpen">
      <header class="panel-header">
        <h2>{{ title }}</h2>
        <button class="close-btn" (click)="close()">&times;</button>
      </header>
      <div class="panel-content">
        <ng-content></ng-content>
      </div>
    </div>
  `,
  styles: [`
    .overlay {
      position: fixed;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      background: rgba(0, 0, 0, 0.2);
      backdrop-filter: blur(2px);
      z-index: 1000;
      transition: opacity 0.3s ease;
    }

    .panel {
      position: fixed;
      top: 0;
      right: -100%;
      width: 100%;
      max-width: 480px;
      height: 100%;
      background: white;
      z-index: 1001;
      box-shadow: -4px 0 24px rgba(0, 0, 0, 0.05);
      transition: right 0.3s cubic-bezier(0.4, 0, 0.2, 1);
      display: flex;
      flex-direction: column;
    }

    .panel.open {
      right: 0;
    }

    .panel-header {
      padding: var(--spacing-lg) var(--spacing-xl);
      border-bottom: 1px solid var(--border-color);
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    .panel-header h2 {
      font-size: 1.25rem;
      font-weight: 600;
      color: var(--primary);
    }

    .close-btn {
      font-size: 2rem;
      color: var(--text-muted);
      line-height: 1;
      padding: var(--spacing-sm);
    }

    .close-btn:hover {
      color: var(--text-main);
    }

    .panel-content {
      flex: 1;
      padding: var(--spacing-xl);
      overflow-y: auto;
    }
  `]
})
export class SidePanelComponent {
  @Input() isOpen = false;
  @Input() title = '';
  @Output() onClose = new EventEmitter<void>();

  close() {
    this.onClose.emit();
  }
}
