import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { FilmsResponse } from '../models/interfaces/films-response';
import { Constants } from '../utils/constants';


const DEFAULT_HEADERS = {
  headers: new HttpHeaders({
    'Content-Type': 'application/json',
    'Authorization': 'Bearer ' + localStorage.getItem('token')
  })
};

@Injectable({
  providedIn: 'root'
})
export class FilmsService {

  constructor(
    private http: HttpClient
  ) { }

  getFilms() : Observable<FilmsResponse>{
    let requestUrl = `${Constants.baseUrl}/films/`;
    return this.http.get<FilmsResponse>(requestUrl, DEFAULT_HEADERS,);

  }

}
