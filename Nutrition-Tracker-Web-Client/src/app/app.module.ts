import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { CommonModule } from "@angular/common";
import { AppRoutingModule } from './app-routing.module';
import { FormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { AppComponent } from './app.component';
import { UserService } from './services/user.service';
import { ContactService } from './services/contact.service';
import { HeaderComponent } from './components/header/header.component';
import { FooterComponent } from './components/footer/footer.component';
import { ContactComponent } from './pages/contact/contact.component';
import { LoginComponent } from './pages/login/login.component';
import { SignupComponent } from './pages/signup/signup.component';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
//import { library } from '@fortawesome/fontawesome-svg-core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { UserComponent } from './pages/user/user.component';
import { UserListComponent } from './pages/user-list/user-list.component';
import { AboutComponent } from './pages/about/about.component';
import { AuthenticationService } from './services/authentication.service';
import { AuthInterceptor } from './interceptors/auth.interceptor';
import { NotificationModule } from './notification.module';
import { NotificationService } from './services/notification.service';
import { MealPlannerComponent } from './pages/meal-planner/meal-planner.component';
import { HealthCalculatorComponent } from './pages/health-calculator/health-calculator.component';
import { InfoComponent } from './pages/info/info.component';
import { EntryService } from './services/entry.service';

@NgModule({
  declarations: [
    AppComponent,
    FooterComponent,
    HeaderComponent,
    AboutComponent,
    ContactComponent,
    LoginComponent,
    SignupComponent,
    UserComponent,
    UserListComponent,
    MealPlannerComponent,
    HealthCalculatorComponent,
    InfoComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    FontAwesomeModule,
    CommonModule,
    BrowserAnimationsModule,
    NotificationModule,
  ],
  providers: [EntryService, NotificationService, UserService, ContactService, AuthenticationService, { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true }],
  bootstrap: [AppComponent]
})
export class AppModule { 

  constructor(){
    //library.add(faEnvelope, faLock);
  }
}
