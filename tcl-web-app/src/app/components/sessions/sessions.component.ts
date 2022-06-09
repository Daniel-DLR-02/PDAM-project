import { Session } from './../../models/interfaces/sessions-response';
import { Subscription } from 'rxjs';
import { Component, OnInit } from '@angular/core';
import { PageEvent } from '@angular/material/paginator';
import { SessionsService } from 'src/app/services/sessions.service';
import { MatDialog } from '@angular/material/dialog';
import { DeleteFilmDialogComponent } from '../films/delete-film-dialog/delete-film-dialog.component';
import { ToastrService } from 'ngx-toastr';
import { DeleteSesssionDialogComponent } from './delete-sesssion-dialog/delete-sesssion-dialog.component';
import { SessionsFormComponent } from './sessions-form/sessions-form.component';

@Component({
  selector: 'app-sessions',
  templateUrl: './sessions.component.html',
  styleUrls: ['./sessions.component.css']
})
export class SessionsComponent implements OnInit {

  filterSearch!:String;
  sessions: Session[] = [];
  sessionsToView: Session[] = [];
  lowValue = 0;
  highValue = 5;
  subscriptions: Subscription[] = [];

  constructor(
    public dialog: MatDialog,
    private sessionsService: SessionsService,
    private toastr: ToastrService
  ) { }

  ngOnInit(): void {

    this.filterSearch = '';
    this.subscriptions.push(
      this.sessionsService.getSessions().subscribe((sessions) => {
        this.sessions = sessions.content;
        this.sessionsToView = this.sessions;
      })
    );
  }

  filterSessions(){
    this.sessionsToView = [];

    if (!(this.filterSearch == '')) {
      this.sessions.forEach((f) => {
        if (
          f['filmTitle'].toLowerCase().includes(this.filterSearch.toLowerCase()) &&
          !this.sessionsToView.includes(f)
        ) {
          this.sessionsToView.push(f);
        }
      });
    } else if (
      this.filterSearch === '' ||
      this.filterSearch === '<empty string>'
    ) {
      this.sessionsToView = this.sessions;
    }
  }

  newSession(){
    const dialogRef = this.dialog.open(SessionsFormComponent, {
      width: '450px',
      data: {
        created: false
      }
    });
    dialogRef.afterClosed().subscribe(result => {
      this.delay(700).then(() => {
        this.ngOnInit();
      });
      if(result?.created)
        this.toastr.success('Sesión creada con éxito');
    });

  }

  editSession(session: Session){
    const dialogRef = this.dialog.open(SessionsFormComponent, {
      width: '450px',
      data: {
        created: false,
        session: session
      }
    });
    dialogRef.afterClosed().subscribe(result => {
      this.delay(700).then(() => {
        this.ngOnInit();
      });
      if(result?.created)
        this.toastr.success('Sesión editada con éxito');
    });

  }


  delay(ms: number) {
    return new Promise((resolve) => setTimeout(resolve, ms));
  }


  openDeleteDialog(sessionUuid: String): void {
    const dialogRef = this.dialog.open(DeleteSesssionDialogComponent, {
      width: '450px',
      data: {
        sessionUuid: sessionUuid,
        borrado: false
      },
    });
    dialogRef.afterClosed().subscribe(result => {
      this.delay(700).then(() => {
        this.ngOnInit();
      });
      if(result?.borrado)
        this.toastr.success('Sesión eliminada con éxito');
    });
  }

  public getPaginatorData(event: PageEvent): PageEvent {
    this.lowValue = event.pageIndex * event.pageSize;
    this.highValue = this.lowValue + event.pageSize;
    return event;
  }
}
