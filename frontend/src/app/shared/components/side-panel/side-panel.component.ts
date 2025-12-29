import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'sp-side-panel',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="overlay" *ngIf="isOpen" (click)="close()"></div>
    <div class="modal" [class.open]="isOpen">
      <header class="modal-header">
        <h2>{{ title }}</h2>
        <button class="close-btn" (click)="close()">&times;</button>
      </header>
      <div class="modal-content">
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
      background: rgba(0, 0, 0, 0.4);
      backdrop-filter: blur(4px);
      z-index: 1000;
      transition: opacity 0.3s ease;
    }

    .modal {
      position: fixed;
      top: 50%;
      left: 50%;
      transform: translate(-50%, -45%);
      width: 90%;
      max-width: 500px;
      background: white;
      z-index: 1001;
      box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
      border-radius: 12px;
      opacity: 0;
      visibility: hidden;
      transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
      display: flex;
      flex-direction: column;
      max-height: 90vh;
    }

    .modal.open {
      opacity: 1;
      visibility: visible;
      transform: translate(-50%, -50%);
    }

    .modal-header {
      padding: var(--spacing-lg) var(--spacing-xl);
      border-bottom: 1px solid var(--border-color);
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    .modal-header h2 {
      font-size: 1.25rem;
      font-weight: 600;
      color: var(--primary);
      margin: 0;
    }

    .close-btn {
      font-size: 1.5rem;
      color: var(--text-muted);
      line-height: 1;
      padding: var(--spacing-sm);
      display: flex;
      align-items: center;
      justify-content: center;
      width: 32px;
      height: 32px;
      border-radius: 50%;
      transition: background 0.2s;
    }

    .close-btn:hover {
      background: var(--bg-surface);
      color: var(--text-main);
    }

    .modal-content {
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
