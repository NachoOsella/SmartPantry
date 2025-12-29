export interface AuthResponse {
  token: string;
  username: string;
  roles: string[];
}

export interface LoginRequest {
  username: string;
  password: string;
}

export interface User {
  username: string;
  roles: string[];
}
