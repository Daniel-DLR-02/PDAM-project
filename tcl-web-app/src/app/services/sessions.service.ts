import { HttpClient, HttpHeaders, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CreateSessionDto } from '../models/dto/create-session';
import { Session, SessionsResponse } from '../models/interfaces/sessions-response';
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
    let requestUrl = `${Constants.baseUrl}/session/`;
    return this.http.get<SessionsResponse>(requestUrl, this.DEFAULT_HEADERS);
  }

  deleteSession(uuid: String): Observable<{}> {
    let requestUrl = `${Constants.baseUrl}/session/${uuid}`;
    return this.http.delete(requestUrl, this.DEFAULT_HEADERS);
  }

  createSession(session: CreateSessionDto): Observable<Session> {
    let requestUrl = `${Constants.baseUrl}/session/`;
    return this.http.post<Session>(requestUrl, session, this.DEFAULT_HEADERS,);

  }

  editSession(session: CreateSessionDto,id: String): Observable<Session> {
    let requestUrl = `${Constants.baseUrl}/session/${id}`;
    return this.http.put<Session>(requestUrl, session, this.DEFAULT_HEADERS,);

  }

}
