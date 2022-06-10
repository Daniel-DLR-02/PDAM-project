import { Hall, HallsResponse } from '../models/interfaces/halls-response';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Constants } from '../utils/constants';
import { CreateHallDto } from '../models/dto/create-hall';

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

  deleteHall(uuid: String): Observable<{}> {
    let requestUrl = `${Constants.baseUrl}/hall/${uuid}`;
    return this.http.delete(requestUrl, this.DEFAULT_HEADERS);
  }

  createHall(newHall: CreateHallDto): Observable<Hall>{
    let requestUrl = `${Constants.baseUrl}/hall/`;
    return this.http.post<Hall>(requestUrl,newHall, this.DEFAULT_HEADERS);
  }

  editHall(editHall: CreateHallDto,uuid:String): Observable<Hall>{
    let requestUrl = `${Constants.baseUrl}/hall/${uuid}`;
    return this.http.put<Hall>(requestUrl,editHall, this.DEFAULT_HEADERS);
  }
}
