import { Component, Input, forwardRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ControlValueAccessor, NG_VALUE_ACCESSOR, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'sp-input',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => InputComponent),
      multi: true
    }
  ],
  template: `
    <div class="input-container">
      <label *ngIf="label" [for]="id">{{ label }}</label>
      <input [id]="id"
             [type]="type"
             [placeholder]="placeholder"
             [value]="value"
             (input)="handleInput($event)"
             (blur)="onTouched()"
             [disabled]="disabled"
             class="input-field">
      <span class="error-msg" *ngIf="error">{{ error }}</span>
    </div>
  `,
  styles: [`
    .input-container {
      display: flex;
      flex-direction: column;
      gap: 0.375rem;
      margin-bottom: 1rem;
    }

    label {
      font-size: 0.75rem;
      font-weight: 600;
      color: var(--text-muted);
      text-transform: uppercase;
      letter-spacing: 0.025em;
    }

    .input-field {
      padding: 0.625rem 0.875rem;
      border: 1px solid var(--border-color);
      border-radius: 4px;
      font-size: 0.875rem;
      transition: all 0.2s ease;
      background-color: white;
    }

    .input-field:focus {
      outline: none;
      border-color: var(--border-focus);
    }

    .error-msg {
      font-size: 0.75rem;
      color: var(--error);
    }
  `]
})
export class InputComponent implements ControlValueAccessor {
  @Input() label = '';
  @Input() type = 'text';
  @Input() placeholder = '';
  @Input() id = 'input-' + Math.random().toString(36).substring(2, 9);
  @Input() error = '';
  @Input() disabled = false;

  value = '';
  onChange: any = () => {};
  onTouched: any = () => {};

  handleInput(event: any) {
    this.value = event.target.value;
    this.onChange(this.value);
  }

  writeValue(value: any): void {
    this.value = value || '';
  }

  registerOnChange(fn: any): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: any): void {
    this.onTouched = fn;
  }

  setDisabledState(isDisabled: boolean): void {
    this.disabled = isDisabled;
  }
}
