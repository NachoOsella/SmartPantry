import { HttpInterceptorFn, HttpHandlerFn, HttpRequest } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { catchError, switchMap, throwError, Subject, of, tap, filter } from 'rxjs';

let isRefreshing = false;
let refreshToken$ = new Subject<string | null>();

export const jwtInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const token = authService.getToken();

  if (token) {
    req = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
  }

  return next(req).pipe(
    catchError(error => {
      if (error.status === 401 && !req.url.includes('/refresh')) {
        return handle401Error(authService, req, next);
      }
      return throwError(() => error);
    })
  );
};

function handle401Error(authService: AuthService, req: HttpRequest<unknown>, next: HttpHandlerFn) {
  if (!isRefreshing) {
    isRefreshing = true;
    refreshToken$.next(null);

    return authService.refreshToken().pipe(
      switchMap((response) => {
        isRefreshing = false;
        const token = response.token;
        refreshToken$.next(token);
        return next(addToken(req, token));
      }),
      catchError((err) => {
        isRefreshing = false;
        refreshToken$.next(null);
        authService.logout();
        return throwError(() => err);
      })
    );
  } else {
    return refreshToken$.pipe(
      filter(token => token !== null),
      tap(token => console.log('Using refreshed token')),
      switchMap(token => next(addToken(req, token!)))
    );
  }
}

function addToken(req: HttpRequest<unknown>, token: string): HttpRequest<unknown> {
  return req.clone({
    setHeaders: {
      Authorization: `Bearer ${token}`
    }
  });
}
