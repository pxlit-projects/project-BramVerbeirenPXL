import { TestBed } from '@angular/core/testing';
import { AuthService } from './auth.service';

describe('AuthService', () => {
  let service: AuthService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AuthService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should store username and role in localStorage on login', () => {
    service.login('testuser', 'editor');
    expect(localStorage.getItem('username')).toBe('testuser');
    expect(localStorage.getItem('role')).toBe('editor');
  });

  it('should remove username and role from localStorage on logout', () => {
    service.login('testuser', 'editor');
    service.logout();
    expect(localStorage.getItem('username')).toBeNull();
    expect(localStorage.getItem('role')).toBeNull();
  });

  it('should return correct role', () => {
    service.login('testuser', 'user');
    expect(service.getRole()).toBe('user');
  });

  it('should return current user', () => {
    service.login('testuser', 'user');
    expect(service.getCurrentUser()).toBe('testuser');
  });

  it('should return true for isEditor if role is editor', () => {
    service.login('editoruser', 'editor');
    expect(service.isEditor()).toBeTrue();
  });

  it('should return true for isUser if role is user', () => {
    service.login('testuser', 'user');
    expect(service.isUser()).toBeTrue();
  });
});
