import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Contact } from '../models/contact';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ContactService {

  private host = environment.contactUrl;

  constructor(private http: HttpClient) { }

  //leave here for testing
  //public findAll(): Observable<Contact[]> { return this.http.get<Contact[]>(`${this.contactUrl}/contact/all`); }

  addContact(contact: Contact): Observable<Contact> {
    return this.http.post<Contact>(`${this.host}/contact/add`, contact);
    }
}
