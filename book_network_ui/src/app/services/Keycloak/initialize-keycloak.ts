import { KeycloakService } from 'keycloak-angular';

export function InitializeKeycloak(keycloak: KeycloakService) {
  return () =>
    keycloak.init({
      config: {
        url: 'http://localhost:9090/',
        realm: 'network',
        clientId: 'frnt-network',
      },
      initOptions: {
        onLoad: 'login-required',             //'login-required',
        silentCheckSsoRedirectUri : window.location.origin + '/assets/silentCheckSso.html',
        checkLoginIframe: false
      },
      enableBearerInterceptor: true,
      bearerPrefix: 'Bearer', 
    });
}
