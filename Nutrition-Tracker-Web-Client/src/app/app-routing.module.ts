import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ContactComponent } from './pages/contact/contact.component';
import { LoginComponent } from './pages/login/login.component';
import { SignupComponent } from './pages/signup/signup.component';
import { AboutComponent } from './pages/about/about.component';
import { UserComponent } from './pages/user/user.component';
import { UserListComponent } from './pages/user-list/user-list.component';
import { AuthenticationGuard } from './guard/authentication.guard';
import { MealPlannerComponent } from './pages/meal-planner/meal-planner.component';
import { InfoComponent } from './pages/info/info.component';
import { HealthCalculatorComponent } from './pages/health-calculator/health-calculator.component';

const routes: Routes = [
  { path: 'contact', component: ContactComponent },
  { path: 'meal-planner', component: MealPlannerComponent },
  { path: 'health-calculator', component: HealthCalculatorComponent },
  { path: 'info', component: InfoComponent },
  { path: 'login', component: LoginComponent },
  { path: 'signup', component: SignupComponent },
  { path: 'about', component: AboutComponent },
  { path: 'user', component: UserComponent, canActivate: [AuthenticationGuard] },
  { path: 'user-list', component: UserListComponent, canActivate: [AuthenticationGuard] },
  { path: '', redirectTo: 'login', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
