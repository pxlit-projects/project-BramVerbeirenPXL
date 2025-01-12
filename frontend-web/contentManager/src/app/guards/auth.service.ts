//auth.service.ts
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private userRole: 'user' | 'editor' | null = null;
  private currentUser: string | null = null;

  login(username: string, role: 'user' | 'editor'): void {
    this.currentUser = username;
    this.userRole = role;
    localStorage.setItem('username', username);
    localStorage.setItem('role', role);
  }

  logout(): void {
    this.currentUser = null;
    this.userRole = null;
    localStorage.removeItem('username');
    localStorage.removeItem('role');
  }

  getRole(): 'user' | 'editor' | null {
    return localStorage.getItem('role') as 'user' | 'editor' | null;
  }

  getCurrentUser(): string | null {
    return localStorage.getItem('username');
  }

  isEditor(): boolean {
    return this.getRole() === 'editor';
  }

  isUser(): boolean {
    return this.getRole() === 'user';
  }
}
