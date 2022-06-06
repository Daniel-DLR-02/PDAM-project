import { Router } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { animate, style, transition, trigger } from '@angular/animations';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css'],
  animations: [
    trigger(
      'openedAnimation',
      [
        transition(
          ':enter',
          [
            style({ width: 60, fontSize: 0 }),
            animate('0.5s ease-out',
                    style({ width: '*',  fontSize: '*' }))
          ]
        ),
        transition(
          ':leave',
          [
            style({ width: 300,  fontSize: 15  }),
            animate('0.5s ease-in',
                    style({ width: 60,  fontSize: 0}))
          ]
        )
      ]
    ),
    trigger(
      'collapsedAnimation',
      [
        transition(
          ':enter',
          [
            style({ width: 0 }),
            animate('0.5s ease-out',
                    style({ width: '*'}))
          ]
        ),
        transition(
          ':leave',
          [
            style({ width: '*' }),
            animate('0.5s ease-in',
                    style({ width: 0}))
          ]
        )
      ],

    )
  ]
})
export class SidebarComponent implements OnInit {

  show!:boolean;

  constructor(
    private router: Router
  ) { }

  ngOnInit(): void {
    this.show = false;
  }

  navigate(route:String){
    this.router.navigate(["/"+route]);
  }
}
