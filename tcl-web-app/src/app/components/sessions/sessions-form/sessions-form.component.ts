import { Session } from './../../../models/interfaces/sessions-response';
import { Component, Inject, OnInit, OnDestroy } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Subscription } from 'rxjs';
import { Film } from 'src/app/models/interfaces/films-response';
import { Hall } from 'src/app/models/interfaces/halls-response';
import { FilmsService } from 'src/app/services/films.service';
import { HallsService } from 'src/app/services/halls.service';
import { SessionsService } from 'src/app/services/sessions.service';

export interface DialogData {
  created: boolean,
  session: Session
}

@Component({
  selector: 'app-sessions-form',
  templateUrl: './sessions-form.component.html',
  styleUrls: ['./sessions-form.component.css']
})
export class SessionsFormComponent implements OnInit,OnDestroy {

  constructor(
    public dialogRef: MatDialogRef<SessionsFormComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData,
    private sessionsService: SessionsService,
    private filmService: FilmsService,
    private hallService: HallsService
  ) { }

  halls: Hall[] = [];
  activeFilms: Film[] = [];
  subscriptions: Subscription[] = [];
  selectedHall!: Hall;
  selectedFilm!:Film

  ngOnInit(): void {
    this.subscriptions.push(
      this.hallService.getHalls().subscribe((h) => {
        this.halls = h.content;
      })
    );

    this.subscriptions.push(
      this.filmService.getActiveFilms().subscribe((f) => {
        this.activeFilms = f.content;
      })
    );

    if(this.data.session){
      // this.selectedHall = this.data.session.
      // this.selectedFilm = this.data.session.filmTitle();
    }


  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(s => s.unsubscribe());
  }


}
