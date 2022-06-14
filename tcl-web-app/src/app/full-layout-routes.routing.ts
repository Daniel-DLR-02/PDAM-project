import { UserFormComponent } from './components/admins/user-form/user-form.component';
import { FilmsFormComponent } from './components/films/films-form/films-form.component';
import { AdminsComponent } from './components/admins/admins.component';
import { HallsComponent } from './components/halls/halls.component';
import { SessionsComponent } from './components/sessions/sessions.component';
import { FilmsComponent } from './components/films/films.component';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';

export const FullLayoutRoutes: Routes = [
  { path: '', redirectTo: 'home', pathMatch: 'full' },
  { path: 'home', component: HomeComponent, pathMatch: 'full' },
  { path: 'films', component: FilmsComponent, pathMatch: 'full'},
  { path: 'films/new-film', component:FilmsFormComponent, pathMatch: 'full' },
  { path: 'films/edit-film', component:FilmsFormComponent, pathMatch: 'full' },
  { path: 'user/new-user', component:UserFormComponent, pathMatch: 'full' },
  { path: 'user/edit-user', component:UserFormComponent, pathMatch: 'full' },
  { path: 'sessions', component: SessionsComponent, pathMatch: 'full' },
  { path: 'halls', component: HallsComponent, pathMatch: 'full' },
  { path: 'admins', component: AdminsComponent, pathMatch: 'full' },



];

