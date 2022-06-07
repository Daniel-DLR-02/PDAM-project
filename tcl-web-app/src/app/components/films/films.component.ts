import { FilmsService } from './../../services/films.service';
import { Subscription } from 'rxjs';
import { Router } from '@angular/router';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { Film } from 'src/app/models/interfaces/films-response';
import { PageEvent } from '@angular/material/paginator';

@Component({
  selector: 'app-films',
  templateUrl: './films.component.html',
  styleUrls: ['./films.component.css']
})
export class FilmsComponent implements OnInit,OnDestroy {

  films: Film[] = [];
  subscriptions: Subscription[]=[]
  lowValue = 0;
  highValue = 5;

  constructor(
    private router: Router,
    private filmsService: FilmsService
  ) { }


  ngOnInit(): void {
    this.subscriptions.push(
      this.filmsService.getFilms().subscribe(
        (films) => {
          console.log(films.content);
          this.films = films.content;
        }
      )
    );
  }

  newFilm(){
    this.router.navigateByUrl('/films/new-film');
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(subscription => subscription.unsubscribe());
  }


  public getPaginatorData(event: PageEvent): PageEvent {
    this.lowValue = event.pageIndex * event.pageSize;
    this.highValue = this.lowValue + event.pageSize;
    return event;
  }

}
