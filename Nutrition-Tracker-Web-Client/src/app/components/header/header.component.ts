import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { faUserCircle } from '@fortawesome/free-solid-svg-icons';
import { User } from 'src/app/models/user';
import { UserComponent } from 'src/app/pages/user/user.component';
import { AuthenticationService } from 'src/app/services/authentication.service';

@Component({
  providers: [UserComponent],
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  constructor(private userComponent: UserComponent, private router: Router, private authenticationService: AuthenticationService) { 
    
  }
  
  isLoggedIn = false;
  faUserCircle = faUserCircle;
  user: User;

  ngOnInit(): void {
    this.isLoggedIn = this.authenticationService.isUserLoggedIn();
    this.router.events.subscribe(event => {
      if(event.constructor.name === "NavigationEnd"){
        this.isLoggedIn = this.authenticationService.isUserLoggedIn();
        this.user = this.authenticationService.getUserFromLocalCache();
      }
    });

    this.user = this.authenticationService.getUserFromLocalCache();
  }
  
  onClick(){
    const navbarMenu = document.querySelector('#nav-links');
    navbarMenu.classList.toggle('is-active');
  }

  /*onDropDown(){
    const dropDown = document.querySelector('#dropDown');
    dropDown.classList.toggle('is-active');
  }*/

  onLogOut(): void{
    this.userComponent.onLogOut();
    this.isLoggedIn = false;
  }
}
