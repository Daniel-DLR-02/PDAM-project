import { AuthLoginResponse } from './../models/interfaces/auth-login-response';
import { Constants } from './../utils/constants';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { LoginDto } from '../models/dto/login';

const DEFAULT_HEADERS = {
  headers: new HttpHeaders({
    'Content-Type': 'application/json'
  })
};

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(
    private http: HttpClient
  ) { }


  login(loginDto:LoginDto) : Observable<AuthLoginResponse>{
    let requestUrl = `${Constants.baseUrl}/auth/login`;
    return this.http.post<AuthLoginResponse>(requestUrl, loginDto, DEFAULT_HEADERS,);

  }
}
