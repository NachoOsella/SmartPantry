import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, throwError } from 'rxjs';
import { Router } from '@angular/router';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const router = inject(Router);

  return next(req).pipe(
    catchError(error => {
      if (error.status === 401) {
        router.navigate(['/login']);
        return throwError(() => ({ ...error, message: 'Session expired. Please log in again.' }));
      }

      if (error.status === 403) {
        return throwError(() => ({ ...error, message: 'You do not have permission to perform this action.' }));
      }

      if (error.status === 404) {
        return throwError(() => ({ ...error, message: 'The requested resource was not found.' }));
      }

      if (error.status === 422) {
        return throwError(() => ({
          ...error,
          message: error.error?.message || 'Validation failed. Please check your input.',
          errors: error.error?.errors
        }));
      }

      if (error.status >= 500) {
        return throwError(() => ({ ...error, message: 'Server error. Please try again later.' }));
      }

      return throwError(() => error);
    })
  );
};
