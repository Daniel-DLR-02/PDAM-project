import { HttpClient, HttpHeaders, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CreateUserDto } from '../models/dto/create-user';
import { EditUserDto } from '../models/dto/edit-user';
import { User, UserResponse } from '../models/interfaces/user-response';
import { Constants } from '../utils/constants';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) {}

  DEFAULT_HEADERS = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json',
      Authorization: 'Bearer ' + localStorage.getItem('tcl-token'),
    }),
  };

  getUsers(): Observable<UserResponse> {
    let requestUrl = `${Constants.baseUrl}/user`;
    return this.http.get<UserResponse>(requestUrl, this.DEFAULT_HEADERS);
  }

  deleteUser(uuid: String): Observable<{}> {
    let requestUrl = `${Constants.baseUrl}/user/${uuid}`;
    return this.http.delete(requestUrl, this.DEFAULT_HEADERS);
  }

  createUserAdmin(user: CreateUserDto, file: File): any {
    let requestUrl = `${Constants.baseUrl}/user/new-admin`;
    const data: FormData = new FormData();
    data.append(
      'user',
      new Blob([JSON.stringify(user)], { type: 'application/json' })
    );
    data.append('file', file);
    const newRequest = new HttpRequest('POST', requestUrl, data, {
      reportProgress: true,
      responseType: 'text',
      headers: new HttpHeaders({
        Authorization: 'Bearer ' + localStorage.getItem('tcl-token'),
      }),
    });
    return this.http.request(newRequest);
  }

  editUser(user: EditUserDto, file: File,id: String): any {
    let requestUrl = `${Constants.baseUrl}/user/${id}`;
    const data: FormData = new FormData();
    data.append(
      'user',
      new Blob([JSON.stringify(user)], { type: 'application/json' })
    );
    data.append('file', file);
    const newRequest = new HttpRequest('PUT', requestUrl, data, {
      reportProgress: true,
      responseType: 'text',
      headers: new HttpHeaders({
        Authorization: 'Bearer ' + localStorage.getItem('tcl-token'),
      }),
    });
    return this.http.request(newRequest);
  }

  editUserNoPoster(user: EditUserDto,id: String): any {
    let requestUrl = `${Constants.baseUrl}/user/${id}`;
    const data: FormData = new FormData();
    data.append(
      'user',
      new Blob([JSON.stringify(user)], { type: 'application/json' })
    );
    const newRequest = new HttpRequest('PUT', requestUrl, data, {
      reportProgress: true,
      responseType: 'text',
      headers: new HttpHeaders({
        Authorization: 'Bearer ' + localStorage.getItem('tcl-token'),
      }),
    });
    return this.http.request(newRequest);
  }

}
