import { FilmsService } from './../../services/films.service';
import { Subscription } from 'rxjs';
import { Router, NavigationExtras } from '@angular/router';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { Film } from 'src/app/models/interfaces/films-response';
import { PageEvent } from '@angular/material/paginator';
import { MatDialog } from '@angular/material/dialog';
import { DeleteFilmDialogComponent } from './delete-film-dialog/delete-film-dialog.component';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-films',
  templateUrl: './films.component.html',
  styleUrls: ['./films.component.css'],
})
export class FilmsComponent implements OnInit, OnDestroy {
  films: Film[] = [];
  subscriptions: Subscription[] = [];
  lowValue = 0;
  highValue = 5;
  filterSearch!: String;
  moviesToView: any[] = [];

  constructor(
    private router: Router,
    private filmsService: FilmsService,
    public dialog: MatDialog,
    private toastr: ToastrService
  ) {}

  ngOnInit() {
    this.filterSearch = '';
    this.subscriptions.push(
      this.filmsService.getFilms().subscribe((films) => {
        this.films = films.content;
        this.moviesToView = this.films;
      })
    );
  }

  newFilm() {
    this.router.navigateByUrl('/films/new-film');
  }

  editFilm(film: Film) {
    const extras: NavigationExtras = {
      state: {
        film: film,
      },
    };
    this.router.navigate(['/films/edit-film'], extras);
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach((subscription) => subscription.unsubscribe());
  }

  public getPaginatorData(event: PageEvent): PageEvent {
    this.lowValue = event.pageIndex * event.pageSize;
    this.highValue = this.lowValue + event.pageSize;
    return event;
  }

  filterMovies() {
    this.moviesToView = [];

    if (!(this.filterSearch == '')) {
      this.films.forEach((f) => {
        if (
          f['title'].toLowerCase().includes(this.filterSearch.toLowerCase()) &&
          !this.moviesToView.includes(f)
        ) {
          this.moviesToView.push(f);
        }
      });
    } else if (
      this.filterSearch === '' ||
      this.filterSearch === '<empty string>'
    ) {
      this.moviesToView = this.films;
    }
  }

  delay(ms: number) {
    return new Promise((resolve) => setTimeout(resolve, ms));
  }

  openDeleteDialog(filmUuid: String, filmTitle: String): void {
    var borrado: boolean;
    const dialogRef = this.dialog.open(DeleteFilmDialogComponent, {
      width: '450px',
      data: {
        filmUuid: filmUuid,
        filmTitle: filmTitle,
        borrado: false
      },
    });
    dialogRef.afterClosed().subscribe(result => {
      this.delay(700).then(() => {
        this.ngOnInit();
      });
      if(result?.borrado)
        this.toastr.success('Película eliminada con éxito');
    });
  }
}
