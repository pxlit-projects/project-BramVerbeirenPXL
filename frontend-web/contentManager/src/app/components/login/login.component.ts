import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../guards/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  imports: [CommonModule, FormsModule],
})
export class LoginComponent {
  username: string = '';
  role: 'user' | 'editor' = 'user';

  constructor(private authService: AuthService, private router: Router) {}

  login(): void {
    if (!this.username.trim()) {
      return;
    }
    this.authService.login(this.username, this.role);
    this.router.navigate(['/dashboard']);
  }
}
