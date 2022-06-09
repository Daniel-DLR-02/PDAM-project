import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { SessionsResponse } from '../models/interfaces/sessions-response';
import { Constants } from '../utils/constants';

@Injectable({
  providedIn: 'root'
})
export class SessionsService {

  constructor(private http: HttpClient) {}

  DEFAULT_HEADERS = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json',
      Authorization: 'Bearer ' + localStorage.getItem('tcl-token'),
    }),
  };

  getSessions(): Observable<SessionsResponse> {
    let requestUrl = `${Constants.baseUrl}/sessions/`;
    return this.http.get<SessionsResponse>(requestUrl, this.DEFAULT_HEADERS);
  }

  deleteSession(uuid: String): Observable<{}> {
    let requestUrl = `${Constants.baseUrl}/sessions/${uuid}`;
    return this.http.delete(requestUrl, this.DEFAULT_HEADERS);
  }

  /*createSession(session: CreateSessionDto, file: File): any {
    let requestUrl = `${Constants.baseUrl}/sessions/`;
    const data: FormData = new FormData();
    data.append(
      'session',
      new Blob([JSON.stringify(session)], { type: 'application/json' })
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

  /*editSession(session: CreateSessionDto, file: File,id: String): any {
    let requestUrl = `${Constants.baseUrl}/sessions/${id}`;
    const data: FormData = new FormData();
    data.append(
      'session',
      new Blob([JSON.stringify(session)], { type: 'application/json' })
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
  }*/

}
