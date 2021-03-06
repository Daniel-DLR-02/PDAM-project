import { Router } from '@angular/router';
import { CreateSessionDto } from './../../../models/dto/create-session';
import { Session } from './../../../models/interfaces/sessions-response';
import { Component, Inject, OnInit, OnDestroy } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Subscription } from 'rxjs';
import { Film } from 'src/app/models/interfaces/films-response';
import { Hall } from 'src/app/models/interfaces/halls-response';
import { FilmsService } from 'src/app/services/films.service';
import { HallsService } from 'src/app/services/halls.service';
import { SessionsService } from 'src/app/services/sessions.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { min } from 'moment';
import * as moment from 'moment';

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
    private hallService: HallsService,
    private formBuilder: FormBuilder,
    private toastr: ToastrService,
    private router: Router
  ) {
    this.form = this.formBuilder.group({
      film: [null, [Validators.required]],
      hall: [null, [Validators.required]],
      time: [null, [Validators.required]]
    });

    if(this.data.session){
      this.isEdit=true;
      this.form.patchValue({
        film: this.data.session.filmUuid,
        hall: this.data.session.hallUuid,
        time: this.data.session.sessionDate,
      });
    }
  }

  form!: FormGroup;
  halls: Hall[] = [];
  activeFilms: Film[] = [];
  subscriptions: Subscription[] = [];
  isEdit: boolean=false;

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


  }

  /*changeMinMaxDate(){


    var min= moment(currentFilm.releaseDate).toISOString().slice(0, 16);
    var max= moment(currentFilm.expirationDate).toISOString().slice(0, 16);
    const timeInput = document.getElementsByName("timeInput")[0] as HTMLInputElement |undefined;
    if(timeInput!=null){
      timeInput.min = min;
      timeInput.max = max;
    }
  }*/

  ngOnDestroy(): void {
    this.subscriptions.forEach(s => s.unsubscribe());
  }

  createSession() {

    var currentFilm!:Film;

    this.activeFilms.forEach((f) => {
      if(f.uuid == this.form.controls["film"].value){
        currentFilm = f
      }
    });

    if (this.form.valid && currentFilm.releaseDate <= this.form.controls["time"].value
    && currentFilm.expirationDate >= this.form.controls["time"].value) {
      const newSession: CreateSessionDto = new CreateSessionDto(
        this.form.controls['film'].value,
        this.form.controls['time'].value,
        this.form.controls['hall'].value,
        true
      );

      this.subscriptions.push(
        this.sessionsService
          .createSession(newSession)
          .subscribe((res: Session) => {
            if (res) {
              this.toastr.success('Sesi??n creada');
              this.closeDialog();
            }
          })
      );
    }
    else if(currentFilm.releaseDate < this.form.controls["time"].value
    || currentFilm.expirationDate > this.form.controls["time"].value){
      this.toastr.error('La fecha de la sesi??n debe estar entre la fecha de estreno y caducidad de la pel??cula', 'Error');
    }
    else {
      this.toastr.error('Por favor, rellene todos los campos', 'Error');
    }
  }

  closeDialog(): void {
    this.dialogRef.close();
  }

  editSession() {

    var currentFilm!:Film;

    this.activeFilms.forEach((f) => {
      if(f.uuid == this.form.controls["film"].value){
        currentFilm = f
      }
    });

    if (this.form.valid && currentFilm.releaseDate <= this.form.controls["time"].value
    && currentFilm.expirationDate >= this.form.controls["time"].value) {
      const editSession: CreateSessionDto = new CreateSessionDto(
        this.form.controls['film'].value,
        this.form.controls['time'].value,
        this.form.controls['hall'].value,
        true
      );

      this.subscriptions.push(
        this.sessionsService
          .editSession(editSession,this.data.session.sessionId)
          .subscribe((res: Session) => {
            if (res) {
              this.toastr.success('Sesi??n editada');
              this.closeDialog();
            }
          })
      );
    }
    else if(currentFilm.releaseDate < this.form.controls["time"].value
    || currentFilm.expirationDate > this.form.controls["time"].value){
      this.toastr.error('La fecha de la sesi??n debe estar entre la fecha de estreno y caducidad de la pel??cula', 'Error');
    }
    else {
      this.toastr.error('Por favor, rellene todos los campos', 'Error');
    }
  }

}
