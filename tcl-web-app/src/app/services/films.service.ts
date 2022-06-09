import { HttpClient, HttpHeaders, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CreateFilmDto } from '../models/dto/create-film';
import { Film, FilmsResponse } from '../models/interfaces/films-response';
import { Constants } from '../utils/constants';

@Injectable({
  providedIn: 'root',
})
export class FilmsService {
  constructor(private http: HttpClient) {}

  DEFAULT_HEADERS = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json',
      Authorization: 'Bearer ' + localStorage.getItem('tcl-token'),
    }),
  };

  getFilms(): Observable<FilmsResponse> {
    let requestUrl = `${Constants.baseUrl}/films/`;
    return this.http.get<FilmsResponse>(requestUrl, this.DEFAULT_HEADERS);
  }

  deleteFilm(uuid: String): Observable<{}> {
    let requestUrl = `${Constants.baseUrl}/films/${uuid}`;
    return this.http.delete(requestUrl, this.DEFAULT_HEADERS);
  }

  createFilm(film: CreateFilmDto, file: File): any {
    let requestUrl = `${Constants.baseUrl}/films/`;
    const data: FormData = new FormData();
    data.append(
      'film',
      new Blob([JSON.stringify(film)], { type: 'application/json' })
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

  editFilm(film: CreateFilmDto, file: File,id: String): any {
    let requestUrl = `${Constants.baseUrl}/films/${id}`;
    const data: FormData = new FormData();
    data.append(
      'film',
      new Blob([JSON.stringify(film)], { type: 'application/json' })
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

  editFilmNoPoster(film: CreateFilmDto,id: String): any {
    let requestUrl = `${Constants.baseUrl}/films/${id}`;
    const data: FormData = new FormData();
    data.append(
      'film',
      new Blob([JSON.stringify(film)], { type: 'application/json' })
    );
    //data.append('file', '');
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
