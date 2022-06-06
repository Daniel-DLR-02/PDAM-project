import { FilmsService } from './../../services/films.service';
import { Subscription } from 'rxjs';
import { Router } from '@angular/router';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { Film } from 'src/app/models/interfaces/films-response';

@Component({
  selector: 'app-films',
  templateUrl: './films.component.html',
  styleUrls: ['./films.component.css']
})
export class FilmsComponent implements OnInit,OnDestroy {

  films: Film[] = [];
  subscriptions: Subscription[]=[]


  constructor(
    private router: Router,
    private filmsService: FilmsService
  ) { }


  ngOnInit(): void {
    this.subscriptions.push(
      this.filmsService.getFilms().subscribe(
        (films) => {
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


}
