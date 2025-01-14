//app.component.ts
import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { RouterModule } from '@angular/router';
import { AuthService } from './guards/auth.service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';


@Component({
  selector: 'app-root',
  imports: [RouterOutlet, RouterModule, CommonModule],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent {
  title = 'contentManager';

  constructor(public authService: AuthService, private router: Router) {}

  get isLoggedIn(): boolean {
    return !!this.authService.getCurrentUser();
  }
  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}