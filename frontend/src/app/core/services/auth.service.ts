import { Injectable, signal, computed } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { AuthResponse, LoginRequest, RegisterRequest, User } from '../models/auth.model';
import { tap, catchError, throwError, Observable } from 'rxjs';
import { Router } from '@angular/router';
import { environment } from '../../../environments/environment';

interface JwtPayload {
  sub: string;
  exp: number;
  iat: number;
  roles: string[];
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly apiUrl = `${environment.apiUrl}/auth`;
  private readonly TOKEN_KEY = 'sp_token';
  private readonly USER_KEY = 'sp_user';

  private _currentUser = signal<User | null>(this.getStoredUser());
  currentUser = computed(() => this._currentUser());
  isAuthenticated = computed(() => !!this._currentUser() && !this.isTokenExpired());

  constructor(private http: HttpClient, private router: Router) {}

  register(data: RegisterRequest) {
    return this.http.post<void>(`${this.apiUrl}/register`, data);
  }

  login(credentials: LoginRequest) {
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, credentials).pipe(
      tap(response => {
        this.setSession(response);
      }),
      catchError(err => {
        this.logout();
        return throwError(() => err);
      })
    );
  }

  logout() {
    localStorage.removeItem(this.TOKEN_KEY);
    localStorage.removeItem(this.USER_KEY);
    this._currentUser.set(null);
    this.router.navigate(['/login']);
  }

  getToken(): string | null {
    const token = localStorage.getItem(this.TOKEN_KEY);
    if (token && this.isTokenExpired()) {
      this.logout();
      return null;
    }
    return token;
  }

  refreshToken(): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/refresh`, {});
  }

  private setSession(authResponse: AuthResponse) {
    const user: User = {
      username: authResponse.username,
      roles: authResponse.roles
    };
    
    localStorage.setItem(this.TOKEN_KEY, authResponse.token);
    localStorage.setItem(this.USER_KEY, JSON.stringify(user));
    this._currentUser.set(user);
  }

  private getStoredUser(): User | null {
    try {
      const userJson = localStorage.getItem(this.USER_KEY);
      if (!userJson) return null;

      const parsedUser = JSON.parse(userJson);
      
      if (!this.isValidUser(parsedUser)) {
        this.logout();
        return null;
      }

      return {
        username: String(parsedUser.username || ''),
        roles: Array.isArray(parsedUser.roles) ? parsedUser.roles : []
      };
    } catch (error) {
      this.logout();
      return null;
    }
  }

  private isValidUser(user: any): boolean {
    return user && 
           typeof user === 'object' && 
           typeof user.username === 'string' && 
           Array.isArray(user.roles) &&
           user.roles.every((role: any) => typeof role === 'string');
  }

  private isTokenExpired(): boolean {
    const token = localStorage.getItem(this.TOKEN_KEY);
    if (!token) return true;

    try {
      const payload: JwtPayload = this.decodeJwt(token);
      const now = Date.now() / 1000;
      return payload.exp < now;
    } catch {
      return true;
    }
  }

  private decodeJwt(token: string): JwtPayload {
    const parts = token.split('.');
    if (parts.length !== 3) throw new Error('Invalid token format');
    
    const payload = parts[1];
    const decoded = atob(payload);
    return JSON.parse(decoded);
  }
}
