import { RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { LoginComponent } from './components/login/login.component';


import { environment } from 'src/environments/environment';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { MaterialImportsModule } from './modules/material-imports.module';
import { AppComponent } from './app.component';
import { ToastrModule } from 'ngx-toastr';
import { HomeComponent } from './components/home/home.component';
import { FullLayoutComponent } from './core/full-layout/full-layout.component';
import { FilmsComponent } from './components/films/films.component';
import { SidebarComponent } from './components/shared/sidebar/sidebar.component';
import { HallsComponent } from './components/halls/halls.component';
import { SessionsComponent } from './components/sessions/sessions.component';
import { AdminsComponent } from './components/admins/admins.component';
import { FilmsFormComponent } from './components/films/films-form/films-form.component';
import { DeleteFilmDialogComponent } from './components/films/delete-film-dialog/delete-film-dialog.component';
import { DeleteSessionDialogComponent } from './components/sessions/delete-sesssion-dialog/delete-sesssion-dialog.component';
import { SessionsFormComponent } from './components/sessions/sessions-form/sessions-form.component';
import { DeleteHallDialogComponent } from './components/halls/delete-hall-dialog/delete-hall-dialog.component';
import { HallFormComponent } from './components/halls/hall-form/hall-form.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    HomeComponent,
    FullLayoutComponent,
    FilmsComponent,
    SidebarComponent,
    HallsComponent,
    SessionsComponent,
    AdminsComponent,
    FilmsFormComponent,
    DeleteFilmDialogComponent,
    DeleteSessionDialogComponent,
    SessionsFormComponent,
    DeleteHallDialogComponent,
    HallFormComponent,
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    MaterialImportsModule,
    ReactiveFormsModule,
    HttpClientModule,
    FormsModule,
    AppRoutingModule,
    ToastrModule.forRoot(),
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
