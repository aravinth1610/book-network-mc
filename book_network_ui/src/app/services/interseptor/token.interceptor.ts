import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpHeaders,
  HttpErrorResponse,
} from '@angular/common/http';
import { catchError, Observable, throwError } from 'rxjs';
import { KeycloakService } from 'keycloak-angular';
import { Router } from '@angular/router';

@Injectable()
export class TokenInterceptor implements HttpInterceptor {
  constructor(
    private keycloakService: KeycloakService,
    private readonly route: Router
  ) {}

  intercept(
    request: HttpRequest<unknown>,
    next: HttpHandler
  ): Observable<HttpEvent<unknown>> {
    const token = this.keycloakService.getToken();
    let httpHeaders = new HttpHeaders();

    if (token) {
      httpHeaders = httpHeaders.append('Authorization', `Bearer ${token}`);
    }
    httpHeaders = httpHeaders.append('X-Requested-With', 'XMLHttpRequest');
    const authReq = request.clone({ headers: httpHeaders });
    return next.handle(authReq).pipe(
      catchError((err: HttpEvent<any>) => {
        if (err instanceof HttpErrorResponse) {
          switch (err.status) {
            case 403:
              this.route.navigate(['forbidden']);
              break;
          }
        }
        return throwError(() => {
          this.route.navigate(['/forbidden']);
        });
      })
    );
  }
}
