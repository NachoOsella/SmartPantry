import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'sp-side-panel',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './side-panel.component.html',
  styleUrls: ['./side-panel.component.css']
})
export class SidePanelComponent {
  @Input() isOpen = false;
  @Input() title = '';
  @Output() onClose = new EventEmitter<void>();

  close() {
    this.onClose.emit();
  }
}
