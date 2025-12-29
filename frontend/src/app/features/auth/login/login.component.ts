import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { ButtonComponent } from '../../../shared/components/button/button.component';
import { InputComponent } from '../../../shared/components/input/input.component';
import { CardComponent } from '../../../shared/components/card/card.component';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CommonModule, 
    ReactiveFormsModule, 
    ButtonComponent, 
    InputComponent, 
    CardComponent
  ],
  template: `
    <div class="login-container">
      <div class="login-box">
        <header class="login-header">
          <h1>SmartPantry</h1>
          <p>Manage your inventory with precision.</p>
        </header>

        <sp-card>
          <form [formGroup]="loginForm" (ngSubmit)="onSubmit()">
            <sp-input
              id="username"
              label="Username"
              placeholder="Enter your username"
              formControlName="username"
              [error]="loginForm.get('username')?.touched && loginForm.get('username')?.invalid ? 'Username is required' : ''"
            ></sp-input>

            <sp-input
              id="password"
              label="Password"
              type="password"
              placeholder="Enter your password"
              formControlName="password"
              [error]="loginForm.get('password')?.touched && loginForm.get('password')?.invalid ? 'Password is required' : ''"
            ></sp-input>

            <div class="form-actions">
              <sp-button type="submit" [disabled]="loginForm.invalid || isLoading">
                {{ isLoading ? 'Signing in...' : 'Sign in' }}
              </sp-button>
            </div>

            <div class="error-banner" *ngIf="errorMessage">
              {{ errorMessage }}
            </div>
          </form>
        </sp-card>

        <footer class="login-footer">
          <p>Don't have an account? <a href="/register">Request access</a></p>
        </footer>
      </div>
    </div>
  `,
  styles: [`
    .login-container {
      min-height: 100vh;
      display: flex;
      align-items: center;
      justify-content: center;
      background-color: var(--bg-surface);
      padding: var(--spacing-md);
    }

    .login-box {
      width: 100%;
      max-width: 400px;
    }

    .login-header {
      text-align: center;
      margin-bottom: var(--spacing-xl);
    }

    .login-header h1 {
      color: var(--primary);
      font-size: 2rem;
      font-weight: 700;
      letter-spacing: -0.025em;
      margin-bottom: 0.5rem;
    }

    .login-header p {
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

    .login-footer {
      margin-top: var(--spacing-xl);
      text-align: center;
      font-size: 0.875rem;
      color: var(--text-muted);
    }

    .login-footer a {
      color: var(--primary);
      font-weight: 600;
    }
  `]
})
export class LoginComponent {
  loginForm: FormGroup;
  isLoading = false;
  errorMessage = '';

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.loginForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  onSubmit() {
    if (this.loginForm.valid) {
      this.isLoading = true;
      this.errorMessage = '';
      
      this.authService.login(this.loginForm.value).subscribe({
        next: () => {
          this.router.navigate(['/dashboard']);
        },
        error: (err) => {
          this.isLoading = false;
          this.errorMessage = 'Invalid username or password';
          console.error('Login error', err);
        }
      });
    }
  }
}
