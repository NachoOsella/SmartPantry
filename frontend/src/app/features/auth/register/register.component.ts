import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { NotificationService } from '../../../core/services/notification.service';
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
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private router = inject(Router);
  private notificationService = inject(NotificationService);

  registerForm: FormGroup = this.fb.group({
    username: ['', [Validators.required, Validators.minLength(4)]],
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(6)]]
  });

  isLoading = false;

  onSubmit() {
    if (this.registerForm.valid) {
      this.isLoading = true;
      
      this.authService.register(this.registerForm.value).subscribe({
        next: () => {
          this.notificationService.success('Account created successfully! You can now sign in.');
          this.router.navigate(['/login']);
        },
        error: (err) => {
          this.isLoading = false;
          const message = err.error?.message || 'Failed to create account. Please try again.';
          this.notificationService.error(message);
        }
      });
    }
  }
}
