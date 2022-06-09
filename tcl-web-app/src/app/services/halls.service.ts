import { HallsResponse } from '../models/interfaces/halls-response';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Constants } from '../utils/constants';

@Injectable({
  providedIn: 'root'
})
export class HallsService {

  constructor(private http: HttpClient) {}

  DEFAULT_HEADERS = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json',
      Authorization: 'Bearer ' + localStorage.getItem('tcl-token'),
    }),
  };

  getHalls(): Observable<HallsResponse> {
    let requestUrl = `${Constants.baseUrl}/hall/`;
    return this.http.get<HallsResponse>(requestUrl, this.DEFAULT_HEADERS);
  }

}
