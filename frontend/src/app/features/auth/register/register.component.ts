import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { ButtonComponent } from '../../../shared/components/button/button.component';
import { InputComponent } from '../../../shared/components/input/input.component';
import { CardComponent } from '../../../shared/components/card/card.component';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [
    CommonModule, 
    ReactiveFormsModule, 
    ButtonComponent, 
    InputComponent, 
    CardComponent,
    RouterModule
  ],
  template: `
    <div class="register-container">
      <div class="register-box">
        <header class="register-header">
          <h1>SmartPantry</h1>
          <p>Create an account to start managing your pantry.</p>
        </header>

        <sp-card>
          <form [formGroup]="registerForm" (ngSubmit)="onSubmit()">
            <sp-input
              id="username"
              label="Username"
              placeholder="Choose a username"
              formControlName="username"
              [error]="registerForm.get('username')?.touched && registerForm.get('username')?.invalid ? 'Username is required' : ''"
            ></sp-input>

            <sp-input
              id="email"
              label="Email"
              type="email"
              placeholder="your@email.com"
              formControlName="email"
              [error]="registerForm.get('email')?.touched && registerForm.get('email')?.invalid ? 'Valid email is required' : ''"
            ></sp-input>

            <sp-input
              id="password"
              label="Password"
              type="password"
              placeholder="Create a password"
              formControlName="password"
              [error]="registerForm.get('password')?.touched && registerForm.get('password')?.invalid ? 'Password (min 6 chars) is required' : ''"
            ></sp-input>

            <div class="form-actions">
              <sp-button type="submit" [disabled]="registerForm.invalid || isLoading">
                {{ isLoading ? 'Creating account...' : 'Create account' }}
              </sp-button>
            </div>

            <div class="error-banner" *ngIf="errorMessage">
              {{ errorMessage }}
            </div>

            <div class="success-banner" *ngIf="successMessage">
              {{ successMessage }}
            </div>
          </form>
        </sp-card>

        <footer class="register-footer">
          <p>Already have an account? <a routerLink="/login">Sign in</a></p>
        </footer>
      </div>
    </div>
  `,
  styles: [`
    .register-container {
      min-height: 100vh;
      display: flex;
      align-items: center;
      justify-content: center;
      background-color: var(--bg-surface);
      padding: var(--spacing-md);
    }

    .register-box {
      width: 100%;
      max-width: 400px;
    }

    .register-header {
      text-align: center;
      margin-bottom: var(--spacing-xl);
    }

    .register-header h1 {
      color: var(--primary);
      font-size: 2rem;
      font-weight: 700;
      letter-spacing: -0.025em;
      margin-bottom: 0.5rem;
    }

    .register-header p {
      color: var(--text-muted);
      font-size: 0.875rem;
    }

    .form-actions {
      margin-top: var(--spacing-lg);
    }

    .form-actions sp-button {
      width: 100%;
    }

    ::ng-deep .form-actions button {
      width: 100%;
    }

    .error-banner {
      margin-top: var(--spacing-md);
      padding: var(--spacing-sm);
      background-color: #fff5f5;
      color: var(--error);
      border: 1px solid #feb2b2;
      border-radius: 4px;
      font-size: 0.875rem;
      text-align: center;
    }

    .success-banner {
      margin-top: var(--spacing-md);
      padding: var(--spacing-sm);
      background-color: #f0fff4;
      color: var(--success);
      border: 1px solid #9ae6b4;
      border-radius: 4px;
      font-size: 0.875rem;
      text-align: center;
    }

    .register-footer {
      margin-top: var(--spacing-xl);
      text-align: center;
      font-size: 0.875rem;
      color: var(--text-muted);
    }

    .register-footer a {
      color: var(--primary);
      font-weight: 600;
    }
  `]
})
export class RegisterComponent {
  registerForm: FormGroup;
  isLoading = false;
  errorMessage = '';
  successMessage = '';

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.registerForm = this.fb.group({
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  onSubmit() {
    if (this.registerForm.valid) {
      this.isLoading = true;
      this.errorMessage = '';
      this.successMessage = '';
      
      this.authService.register(this.registerForm.value).subscribe({
        next: () => {
          this.isLoading = false;
          this.successMessage = 'Account created successfully! Redirecting to login...';
          setTimeout(() => {
            this.router.navigate(['/login']);
          }, 2000);
        },
        error: (err) => {
          this.isLoading = false;
          this.errorMessage = err.error?.message || 'Error creating account. Please try again.';
          console.error('Registration error', err);
        }
      });
    }
  }
}
