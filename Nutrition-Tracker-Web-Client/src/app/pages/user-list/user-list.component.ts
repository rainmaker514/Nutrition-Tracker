import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { faClipboard, faDumbbell, faEdit, faEnvelope, faHourglass, faPlus, faRuler, faShieldAlt, faTrash, faUser, faWeight } from '@fortawesome/free-solid-svg-icons';
import { Subscription } from 'rxjs';
import { NotificationType } from 'src/app/enum/notification-type.enum';
import { CustomHttpResponse } from 'src/app/models/custom-http-response';
import { User } from 'src/app/models/user';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { NotificationService } from 'src/app/services/notification.service';
import { UserService } from 'src/app/services/user.service';
import { Role } from 'src/app/enum/role.enum';

@Component({
  selector: 'app-pages-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css']
})
export class UserListComponent implements OnInit {
  private currentEmail: string;
  users: User[];
  user: User;
  selectedUser: User;
  deleteUser: User;
  addUser: User;
  editUser: User = new User();
  public heightDropDown: string[] = [];
  private subscriptions: Subscription[] = [];
  faPlus = faPlus;
  faEdit = faEdit;
  faTrash = faTrash;
  faUser = faUser;
  faClipboard = faClipboard;
  faDumbbell = faDumbbell;
  faHourglass = faHourglass;
  faWeight = faWeight;
  faRuler = faRuler;
  faEnvelope = faEnvelope;
  faShield = faShieldAlt;
  showLoading = false;

  @ViewChild('addUserForm') public addUserForm : NgForm;


  constructor(private userService: UserService, private notificationService: NotificationService, private authenticationService: AuthenticationService) { }

  ngOnInit(): void {
    this.getUsers(true);
  }

  getUsers(showNotification: boolean): void {
    this.subscriptions.push(
      this.userService.findAll().subscribe(
        (response: User[]) => {
          this.userService.addUsersToLocalCache(response);
          this.users = response;
          if(showNotification){
            this.notificationService.notify(NotificationType.SUCCESS, `${response.length} user(s) found.`);
          }
        },
        (errorResponse: HttpErrorResponse) =>{
          this.notificationService.notify(NotificationType.ERROR, errorResponse.error.message);
        }
      )
    );
  }

  onSelectUser(selectedUser: User): void{
    this.selectedUser = selectedUser;
    const view = document.getElementById('viewUserModal');
    view.classList.toggle('is-active');
  }

  onDeleteUser(deleteUser: User): void{
    this.deleteUser = deleteUser;
    //const del = document.getElementById('deleteUserModal');
    //del.classList.toggle('is-active');
    //document.getElementById('delete-btn').addEventListener("click", () => {
      this.subscriptions.push(
        this.userService.deleteUser(deleteUser.email).subscribe(
          (response: CustomHttpResponse) => {
            console.log(response);
            this.notificationService.notify(NotificationType.SUCCESS, response.message);
            this.getUsers(false);
          },
          (errorResponse: HttpErrorResponse) =>{
            this.notificationService.notify(NotificationType.ERROR, errorResponse.error.message);
            console.log(errorResponse);
          }
        )
      );
      //del.classList.toggle('is-active');
    //});
  }
  
  onEditUser(editUser: User): void{
    if(this.isAdmin){
      this.populateHeightArray();
      this.editUser = editUser;
      this.currentEmail = editUser.email;
      document.getElementById('editUserModal').classList.toggle('is-active');
    }else{
      this.notificationService.notify(NotificationType.ERROR, "YOU DON'T HAVE ENOUGH PERMISSION.");
    }
    
  }  

  populateHeightArray(){
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

          this.heightDropDown.push(ft + "\'" + inch++ + '"');
        }
  }

  onUpdateUser(): void{
    const formData = this.userService.createUserFormData(this.currentEmail, this.editUser);
    this.subscriptions.push(
      this.userService.updateUser(formData).subscribe(
        (response: User) => {
          document.getElementById('editUserModal').classList.toggle('is-active');
          this.getUsers(false);
          this.notificationService.notify(NotificationType.SUCCESS, `${response.firstname} ${response.lastname} was updated successfully.`);
        },
        (errorResponse: HttpErrorResponse) =>{
          this.notificationService.notify(NotificationType.ERROR, errorResponse.error.message);
          console.log(errorResponse);
        }
      )
    );
  }  

  onAddUser(addUserForm: NgForm): void{
    const formData = this.userService.createUserFormData(null, addUserForm.value);
    this.subscriptions.push(
      this.userService.addUser(formData).subscribe(
        (response: User) => {
          document.getElementById('addUserClose').click();
          this.getUsers(false);
          addUserForm.reset();
          this.notificationService.notify(NotificationType.SUCCESS, `${response.firstname} ${response.lastname} was added successfully.`);
        },
        (errorResponse: HttpErrorResponse) =>{
          this.notificationService.notify(NotificationType.ERROR, errorResponse.error.message);
          console.log(errorResponse);
        }
      )
    );
  }

  saveNewUser(): void{
    this.showLoading = true;
    document.getElementById('addUserSave').click();
  }

  searchUsers(searchTerm: string): void{
    const results: User[] = [];
    for(const user of this.userService.getUsersFromLocalCache()){
      if(user.firstname.toLowerCase().indexOf(searchTerm.toLowerCase()) !== -1 ||
      user.lastname.toLowerCase().indexOf(searchTerm.toLowerCase()) !== -1 ||
      user.email.toLowerCase().indexOf(searchTerm.toLowerCase()) !== -1){
        results.push(user);
      }
    }
    this.users = results;
    if(results.length === 0 || !searchTerm){
      this.users = this.userService.getUsersFromLocalCache();
    }
  }

  public get isAdmin(): boolean {
    return this.getUserRole() === Role.ADMIN;
  }

  private getUserRole(): string {
    return this.authenticationService.getUserFromLocalCache().role;
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }

  onAddUserModalClose(): void{
    document.getElementById('addUserModal').classList.toggle('is-active');
    this.addUserForm.reset();
  }
}
