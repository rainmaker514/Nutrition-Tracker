import { Component, OnDestroy, OnInit } from '@angular/core';
import { User } from 'src/app/models/user';
import { UserService } from 'src/app/services/user.service';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { NgForm } from '@angular/forms';
import { faEnvelope } from '@fortawesome/free-solid-svg-icons';
import { faLock } from '@fortawesome/free-solid-svg-icons';
import { faRulerVertical } from '@fortawesome/free-solid-svg-icons';
import { faUser } from '@fortawesome/free-solid-svg-icons';
import { faWeight } from '@fortawesome/free-solid-svg-icons';
import { faHourglass } from '@fortawesome/free-solid-svg-icons';
import { faDumbbell } from '@fortawesome/free-solid-svg-icons';
import { faClipboard } from '@fortawesome/free-solid-svg-icons';
import { Router } from '@angular/router';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { NotificationService } from 'src/app/services/notification.service';
import { NotificationType } from 'src/app/enum/notification-type.enum';
import { Subscription } from 'rxjs';
import { HeaderType } from 'src/app/enum/header-type.enum';
import { LoginComponent } from '../login/login.component';


@Component({
  providers: [LoginComponent],
  selector: 'app-pages-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent implements OnInit {

  public users: User[];
  //public heightDropDown: string[] = [];
  faEnvelope = faEnvelope;
  faLock = faLock;
  faRuler = faRulerVertical;
  faUser = faUser;
  faWeight = faWeight;
  faHourglass = faHourglass;
  faDumbbell = faDumbbell;
  faClipboard = faClipboard;
  private subscriptions: Subscription[] = [];

  constructor(private loginComponent: LoginComponent, private notificationService: NotificationService, private userService: UserService, private authenticationService: AuthenticationService, private router: Router) {}

  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }

  ngOnInit(): void {
    if(this.authenticationService.isUserLoggedIn()){
      this.router.navigateByUrl('/user');
    }
    //this.populateHeightArray();
    
  }


  onSignUp(user: User): void {

    //add functionality that navigates user from sign up page
    this.userService.signUp(user).subscribe(
      (response: User) => {
        console.log(response);
        this.notificationService.notify(NotificationType.SUCCESS, `Sign up successful! Welcome, ${response.firstname}!`);
        this.loginComponent.onLogin(user);
      },
      (errorResponse: HttpErrorResponse) => {
        console.log(errorResponse);
        this.notificationService.notify(NotificationType.ERROR, errorResponse.error.message);
      }
    );
  }


  //generate array of heights for dropdown
  /*populateHeightArray(){
    var ft = 4;
        var inch = 0;

        for(let i = 0; i < 36; i++){

          if(ft == 7){
            break;
          }

          if(inch == 12){
            ft++;
            inch = 0;
          }

          this.heightDropDown.push(ft + "\'" + inch++ + "\"");
        }
  }*/

}
