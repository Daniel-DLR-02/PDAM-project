import { HomeComponent } from './components/home/home.component';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { FullLayoutComponent } from './core/full-layout/full-layout.component';
import { FullLayoutRoutes } from './full-layout-routes.routing';
import { AuthGuard } from './auth.guard';

const routes: Routes = [
  { path: 'admin-login', component: LoginComponent, pathMatch: 'full' },
  { path: '', component: FullLayoutComponent, children: FullLayoutRoutes, canActivate: [AuthGuard] },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
