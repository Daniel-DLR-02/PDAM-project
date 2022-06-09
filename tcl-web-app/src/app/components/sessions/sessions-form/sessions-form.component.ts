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

  ngOnDestroy(): void {
    this.subscriptions.forEach(s => s.unsubscribe());
  }

  createSession() {
    if (this.form.valid) {
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
              this.toastr.success('Sesión creada');
              this.closeDialog();
            }
          })
      );
    } else {
      this.toastr.error('Por favor, rellene todos los campos', 'Error');
    }
  }

  closeDialog(): void {
    this.dialogRef.close();
  }

  editSession() {
    if (this.form.valid) {
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
              this.toastr.success('Sesión editada');
              this.closeDialog();
            }
          })
      );
    } else {
      this.toastr.error('Por favor, rellene todos los campos', 'Error');
    }
  }

}
