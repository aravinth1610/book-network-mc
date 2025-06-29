import { APP_INITIALIZER, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BookComponent } from './components/book/book.component';
import { HomeComponent } from './components/home/home.component';
import { HeaderComponent } from './components/header/header.component';
import { HttpClientModule, HttpClientXsrfModule, provideHttpClient, withInterceptorsFromDi } from '@angular/common/http'; 
import { KeycloakAngularModule, KeycloakService } from 'keycloak-angular';
import { LoginComponent } from './components/login/login.component';
import { InitializeKeycloak } from './services/Keycloak/initialize-keycloak';



@NgModule({
  declarations: [
    AppComponent,
    BookComponent,
    HomeComponent,
    HeaderComponent,
    LoginComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    KeycloakAngularModule,
    HttpClientXsrfModule.withOptions({
      cookieName: 'XSRF-TOKEN',
      headerName: 'X-XSRF-TOKEN',
    }),
  ],
  providers: [
    { provide: APP_INITIALIZER, deps: [KeycloakService], useFactory: InitializeKeycloak , multi: true },
    provideHttpClient(withInterceptorsFromDi())

   
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
