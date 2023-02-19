import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { HeaderType } from 'src/app/enum/header-type.enum';
import { NotificationType } from 'src/app/enum/notification-type.enum';
import { User } from 'src/app/models/user';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { NotificationService } from 'src/app/services/notification.service';
import { faLock } from '@fortawesome/free-solid-svg-icons';
import { faEnvelope } from '@fortawesome/free-solid-svg-icons';
import { UserService } from 'src/app/services/user.service';
import { CustomHttpResponse } from 'src/app/models/custom-http-response';

@Component({
  selector: 'app-pages-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit, OnDestroy {
  showLoading: boolean;
  private subscriptions: Subscription[] = [];
  faEnvelope = faEnvelope;
  faLock = faLock;

  constructor(private router: Router, private authenticationService: AuthenticationService, private notificationService: NotificationService, 
    private userService: UserService) { }
  
  ngOnInit(): void {
    if(this.authenticationService.isUserLoggedIn()){
      this.router.navigateByUrl('/user');
    }else{
      this.router.navigateByUrl('/login');
    }
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }

  onLogin(user: User): void {
    this.showLoading = true;
    console.log(user);
    this.subscriptions.push(
      this.authenticationService.logIn(user).subscribe(
        (response: HttpResponse<User>) => {
          const token = response.headers.get(HeaderType.JWT_TOKEN);
          
          this.authenticationService.saveToken(token);
          this.authenticationService.addUserToLocalCache(response.body);
          this.router.navigateByUrl('/user');
          
          this.notificationService.notify(NotificationType.SUCCESS, "Login successful!");
          
          this.showLoading = false;
        },
        (errorResponse: HttpErrorResponse) => {
          console.log(errorResponse);
          this.notificationService.notify(NotificationType.ERROR, errorResponse.error.message);
          this.showLoading = false;
        }
      )
    );
    
  }

  public onResetPassword(emailForm: NgForm): void{
    this.showLoading = true;
    const email = emailForm.value['email'];
    this.subscriptions.push(
      this.userService.resetPassword(email).subscribe(
        (response: CustomHttpResponse) => {
          this.showLoading = false;
          this.notificationService.notify(NotificationType.SUCCESS, response.message);
          document.getElementById('close-btn').click();
        },
        (error: HttpErrorResponse) => {
          this.notificationService.notify(NotificationType.WARNING, error.error.message);
        },
        () => emailForm.reset()
      )
    );
    
  }
  
}
