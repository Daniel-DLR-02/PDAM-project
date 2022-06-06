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
    FilmsFormComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    MaterialImportsModule,
    ReactiveFormsModule,
    HttpClientModule,
    FormsModule,
    AppRoutingModule,
    ToastrModule.forRoot(), // ToastrModule added
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
