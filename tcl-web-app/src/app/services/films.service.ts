import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CreateFilmDto } from '../models/dto/create-film';
import { Film, FilmsResponse } from '../models/interfaces/films-response';
import { Constants } from '../utils/constants';




@Injectable({
  providedIn: 'root'
})
export class FilmsService {

  constructor(
    private http: HttpClient
  ) { }

 DEFAULT_HEADERS = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': 'Bearer ' + localStorage.getItem('tcl-token'),
    })
  };

  CREATE_MOVIE_HEADERS = {
    headers: new HttpHeaders({
      'Authorization': 'Bearer ' + localStorage.getItem('tcl-token'),
      'Content-Type': 'multipart/form-data'
    })
  };



  getFilms() : Observable<FilmsResponse>{
    let requestUrl = `${Constants.baseUrl}/films/`;
    return this.http.get<FilmsResponse>(requestUrl, this.DEFAULT_HEADERS);
  }

  deleteFilm(uuid: String): Observable<{}> {
    let requestUrl = `${Constants.baseUrl}/films/${uuid}`;
    return this.http.delete(requestUrl, this.DEFAULT_HEADERS);
  }

  createFilm(film: CreateFilmDto, file:File): Observable<Film>{
    let requestUrl = `${Constants.baseUrl}/films/`;
    var fd = new FormData();
    //console.log(JSON.stringify(film))
    //fd.append('film', JSON.stringify(film));
    fd.append('film', new Blob([JSON.stringify(film)], { type: 'application/json' }));
    fd.append('file', file);
    return this.http.post<Film>(requestUrl,fd, this.CREATE_MOVIE_HEADERS);
  }

}
