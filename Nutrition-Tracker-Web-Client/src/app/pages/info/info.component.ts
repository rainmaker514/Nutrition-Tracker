import { HttpErrorResponse, HttpParams } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { faHamburger, faStickyNote, faTasks, faPlus, faWeight } from '@fortawesome/free-solid-svg-icons';
import { Subscription } from 'rxjs';
import { NotificationType } from 'src/app/enum/notification-type.enum';
import { Entry } from 'src/app/models/entry';
import { User } from 'src/app/models/user';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { NotificationService } from 'src/app/services/notification.service';
import { UserService } from 'src/app/services/user.service';
import { EntryService } from 'src/app/services/entry.service';

@Component({
  selector: 'app-info',
  templateUrl: './info.component.html',
  styleUrls: ['./info.component.css']
})
export class InfoComponent implements OnInit {

  constructor(private authenticationService: AuthenticationService, private router: Router, private notificationService: NotificationService, 
    private userService: UserService, private entryService: EntryService) { }

  faTasks = faTasks;
  faHamburger = faHamburger;
  faWeight = faWeight;
  faStickyNote = faStickyNote;
  faPlus = faPlus;
  tabId = "my-progress";
  tabArray: string[] = ["my-progress", "my-food", "my-notes"];
  user: User;
  private subscriptions: Subscription[] = [];

  ngOnInit(): void {
    this.user = this.authenticationService.getUserFromLocalCache();
    
  }

  onTabChange(id: any): void{
    document.getElementById(id).classList.add("is-active");

    this.tabArray.forEach((value,index)=>{
      if(value==id) this.tabArray.splice(index,1);
    });

    this.tabArray.forEach(tab => {
      document.getElementById(tab).classList.remove("is-active");
    });

    this.tabArray.push(id);
    this.tabId = id;
  }

  onChangeStartingWeight(changeStartingWeightForm: NgForm): void{
    const startingWeight = changeStartingWeightForm.value['startingWeight'];
    const email = this.user.email;
    let params = new HttpParams()
    .set('email', email)
    .set('startingWeight', startingWeight);
    
    this.subscriptions.push(
      this.userService.changeStartingWeight(params).subscribe(
        (response: User) => {
          this.authenticationService.addUserToLocalCache(response);
          this.user = this.authenticationService.getUserFromLocalCache();
          this.notificationService.notify(NotificationType.SUCCESS, "Weight changed.");
          document.getElementById('close-btn').click();
          
        },
        (error: HttpErrorResponse) => {
          this.notificationService.notify(NotificationType.WARNING, error.error.message);
        },
        () => changeStartingWeightForm.reset()
      )
    );
  }

  onChangeCurrentWeight(changeCurrentWeightForm: NgForm): void{
    const currentWeight = changeCurrentWeightForm.value['currentWeight'];
    const email = this.user.email;
    let params = new HttpParams()
    .set('email', email)
    .set('currentWeight', currentWeight);
    
    this.subscriptions.push(
      this.userService.changeCurrentWeight(params).subscribe(
        (response: User) => {
          console.log(currentWeight);
          this.authenticationService.addUserToLocalCache(response);
          this.user = this.authenticationService.getUserFromLocalCache();
          this.notificationService.notify(NotificationType.SUCCESS, "Weight changed.");
          document.getElementById('close-btn1').click();
        },
        (error: HttpErrorResponse) => {
          this.notificationService.notify(NotificationType.WARNING, error.error.message);
          console.log(currentWeight);
        },
        () => changeCurrentWeightForm.reset()
      )
    );
  }

  onCreateNewEntry(entry: Entry): void{
    
    
        
    this.subscriptions.push(
      this.entryService.addEntry(this.user.id, entry).subscribe(
        (response: Entry) => {
          console.log(response);
          /*this.authenticationService.addUserToLocalCache(response);
          this.user = this.authenticationService.getUserFromLocalCache();*/
          this.notificationService.notify(NotificationType.SUCCESS, "Entry added.");
          document.getElementById('close-btn').click();
        },
        (error: HttpErrorResponse) => {
          this.notificationService.notify(NotificationType.WARNING, "Entry could not be created.");
        },
        () => (document.getElementById('createNewEntryForm')as HTMLFormElement).reset()
      )
    );
  }
}


