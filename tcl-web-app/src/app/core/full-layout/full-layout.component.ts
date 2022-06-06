import { Router } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { Constants } from 'src/app/utils/constants';

@Component({
  selector: 'app-full-layout',
  templateUrl: './full-layout.component.html',
  styleUrls: ['./full-layout.component.css']
})
export class FullLayoutComponent implements OnInit {

  constructor(
    private router: Router
  ) { }

  img!: string;
  nick!: string;

  ngOnInit(): void {
    this.img=localStorage.getItem('tcl-avatar') ?? Constants.defaultUserImage;
    this.nick = localStorage.getItem('tcl-nick') ?? 'Usuario';
  }

  logout() {
    localStorage.removeItem('tcl-token');
    localStorage.removeItem('tcl-avatar');
    localStorage.removeItem('tcl-nick');
    window.location.reload();
  }


  goHome() {
    this.router.navigate(['/home']);
  }

}
