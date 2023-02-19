import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor
} from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthenticationService } from '../services/authentication.service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  constructor(private authenticationService: AuthenticationService) {}
  //intercepts http request and adds header with token to request so paths that require login wont fail
  intercept(httpRequest: HttpRequest<any>, httpHandler: HttpHandler): Observable<HttpEvent<any>> {
    if(httpRequest.url.includes(`${this.authenticationService.host}/users/login`)){
      return httpHandler.handle(httpRequest);
    }
    if(httpRequest.url.includes(`${this.authenticationService.host}/users/signup`)){
      return httpHandler.handle(httpRequest);
    }
    if(httpRequest.url.includes(`${this.authenticationService.host}/users/reset-password`)){
      return httpHandler.handle(httpRequest);
    }
    this.authenticationService.loadToken();
    const token = this.authenticationService.getToken();
    const request = httpRequest.clone({ setHeaders: { Authorization: `Bearer ${token}` } });
    return httpHandler.handle(request);
  }
}
