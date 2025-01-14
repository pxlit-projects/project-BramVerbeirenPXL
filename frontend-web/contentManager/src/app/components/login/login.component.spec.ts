import { ComponentFixture, TestBed } from '@angular/core/testing';
import { LoginComponent } from './login.component';
import { AuthService } from '../../guards/auth.service';
import { RouterTestingModule } from '@angular/router/testing';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { By } from '@angular/platform-browser';

class MockAuthService {
  login(username: string, role: 'user' | 'editor'): void {}
}

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authServiceSpy: jasmine.SpyObj<AuthService>;
  let router: Router;

  beforeEach(async () => {
    authServiceSpy = jasmine.createSpyObj('AuthService', ['login']);

    await TestBed.configureTestingModule({
      imports: [RouterTestingModule, FormsModule, LoginComponent],
      providers: [{ provide: AuthService, useValue: authServiceSpy }],
    }).compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    router = TestBed.inject(Router);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call authService.login on login()', () => {
    component.username = 'testuser';
    component.role = 'editor';

    spyOn(router, 'navigate'); 
    component.login();

    expect(authServiceSpy.login).toHaveBeenCalledWith('testuser', 'editor');
    expect(router.navigate).toHaveBeenCalledWith(['/dashboard']);
  });

  it('should have default role as "user"', () => {
    expect(component.role).toBe('user');
  });

  it('should display input for username and role selection', () => {
    fixture.detectChanges(); 
  
    const usernameInput = fixture.debugElement.query(By.css('input[name="username"]'));
    const selectRole = fixture.debugElement.query(By.css('select[name="role"]'));
  
    expect(usernameInput).toBeTruthy(); 
    expect(selectRole).toBeTruthy(); 
  });

  it('should login as editor and navigate to dashboard', () => {
    spyOn(router, 'navigate');
    component.username = 'editoruser';
    component.role = 'editor';

    component.login();
    expect(authServiceSpy.login).toHaveBeenCalledWith('editoruser', 'editor');
    expect(router.navigate).toHaveBeenCalledWith(['/dashboard']);
  });
});
