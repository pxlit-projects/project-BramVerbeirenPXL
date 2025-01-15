import { Injectable } from '@angular/core';
import {
  HttpInterceptor,
  HttpRequest,
  HttpHandler,
  HttpEvent,
} from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(private authService: AuthService) {}

  intercept(
    req: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    const role = this.authService.getRole();
    const username = this.authService.getCurrentUser();

    if (role && username) {
      const clonedRequest = req.clone({
        setHeaders: {
          'X-User-Role': role,
          'X-User-Name': username,
        },
      });
      return next.handle(clonedRequest);
    }
    return next.handle(req);
  }
}
