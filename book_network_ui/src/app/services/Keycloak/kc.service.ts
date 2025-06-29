import { Injectable } from '@angular/core';
import { KeycloakService } from 'keycloak-angular';
import { KeycloakProfile } from 'keycloak-js';

@Injectable({
  providedIn: 'root'
})
export class KcService {

  public userProfile: KeycloakProfile | undefined;

  constructor( protected readonly keycloak: KeycloakService) { }
  

  get tokenParse() {
    return this.keycloak.getKeycloakInstance().tokenParsed;
  }
  
  get sub(){
    const token = this.tokenParse;
    return token?.sub;
  }

  keyClockLogin() {
    this.keycloak.login();
  }

  keyClockLogout() {
    this.keycloak.logout();
  }

   get IskeyClockLogin() {
    return this.keycloak.isLoggedIn();
  } 


}
