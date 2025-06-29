import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { KeycloakService } from 'keycloak-angular';

export const authGuard: CanActivateFn = (route, state) => {
  const tokenService = inject(KeycloakService);
  const authRoute = inject(Router);
console.log("--")
  if (tokenService.isTokenExpired()) {
    authRoute.navigate(['/login']);
    return false;
  }
  return true;
};
