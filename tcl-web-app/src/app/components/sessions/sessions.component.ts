import { Session } from './../../models/interfaces/sessions-response';
import { Subscription } from 'rxjs';
import { Component, OnInit } from '@angular/core';
import { PageEvent } from '@angular/material/paginator';
import { SessionsService } from 'src/app/services/sessions.service';

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
    private sessionsService: SessionsService
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

  }

  newSession(){

  }

  editSession(session: any){

  }

  openDeleteDialog(uuid: string){

  }

  public getPaginatorData(event: PageEvent): PageEvent {
    this.lowValue = event.pageIndex * event.pageSize;
    this.highValue = this.lowValue + event.pageSize;
    return event;
  }
}
