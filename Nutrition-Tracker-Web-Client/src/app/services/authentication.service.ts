import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { HttpClient, HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { User } from '../models/user';
import { Observable } from 'rxjs/internal/Observable';
import { JwtHelperService } from "@auth0/angular-jwt";

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  public host = environment.userUrl;
  private token: string;
  private loggedInEmail: string;
  private jwtHelper = new JwtHelperService();

  constructor(private http: HttpClient) { }

  logIn(user: User): Observable<HttpResponse<User>> {
    
    return this.http.post<User>(`${this.host}/users/login`, user, {observe: 'response'});
  }

  logout(): void {
        this.token = null;
        this.loggedInEmail = null;
        localStorage.removeItem('user');
        localStorage.removeItem('token');
        localStorage.removeItem('users');
    }

    saveToken(token: string): void {
      this.token = token;
      localStorage.setItem('token', token);
    }

    addUserToLocalCache(user: User): void {
      localStorage.setItem('user', JSON.stringify(user));
    }

    getUserFromLocalCache(): User {
      return JSON.parse(localStorage.getItem('user'));
    }

    loadToken(): void {
      this.token = localStorage.getItem('token');
    }

    getToken(): string {
      return this.token;
    }

    isUserLoggedIn(): boolean {
      this.loadToken();
      if(this.token != null && this.token !== ''){
        if(this.jwtHelper.decodeToken(this.token).sub != null || ''){
          if(!this.jwtHelper.isTokenExpired(this.token)){
            this.loggedInEmail = this.jwtHelper.decodeToken(this.token).sub;
            return true;
          }
        }
      }else{
        this.logout();
        return false;
      }
      return false;
    }
}
