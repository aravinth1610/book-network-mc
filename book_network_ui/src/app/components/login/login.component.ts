import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { KeycloakService } from 'keycloak-angular';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent implements OnInit {
  constructor(private keycloakService: KeycloakService, private readonly authRoute: Router) {}

  async ngOnInit() {
    try {
      // Check if the user is already logged in
      const isAuthenticated = await this.keycloakService.isLoggedIn();
      console.log("======login", isAuthenticated);

      if (!isAuthenticated) {
        // If not logged in, initiate the login flow
        await this.keycloakService.login({
          redirectUri: window.location.origin,  // Adjust redirect URI if needed
        });
      } else {
        // If already logged in, redirect to the home page or other desired page
        await this.keycloakService.login({
          redirectUri: window.location.origin,  // Adjust redirect URI if needed
        });
      }
    } catch (error) {
      console.error('Error during Keycloak login:', error);
    }
  }
}
