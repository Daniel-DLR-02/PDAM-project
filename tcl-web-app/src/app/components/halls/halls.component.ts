import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { PageEvent } from '@angular/material/paginator';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { Hall } from 'src/app/models/interfaces/halls-response';
import { HallsService } from 'src/app/services/halls.service';
import { DeleteHallDialogComponent } from './delete-hall-dialog/delete-hall-dialog.component';
import { HallFormComponent } from './hall-form/hall-form.component';

@Component({
  selector: 'app-halls',
  templateUrl: './halls.component.html',
  styleUrls: ['./halls.component.css']
})
export class HallsComponent implements OnInit {

  filterSearch!:String;
  halls: Hall[] = [];
  hallsToView: Hall[] = [];
  lowValue = 0;
  highValue = 5;
  subscriptions: Subscription[] = [];

  constructor(
    public dialog: MatDialog,
    private hallsService: HallsService,
    private toastr: ToastrService
  ) { }

  ngOnInit(): void {

    this.filterSearch = '';
    this.subscriptions.push(
      this.hallsService.getHalls().subscribe((halls) => {
        this.halls = halls.content;
        this.hallsToView = this.halls;
      })
    );
  }

  filterHalls(){
    this.hallsToView = [];

    if (!(this.filterSearch == '')) {
      this.halls.forEach((f) => {
        if (
          f.name.toLowerCase().includes(this.filterSearch.toLowerCase()) &&
          !this.hallsToView.includes(f)
        ) {
          this.hallsToView.push(f);
        }
      });
    } else if (
      this.filterSearch === '' ||
      this.filterSearch === '<empty string>'
    ) {
      this.hallsToView = this.halls;
    }
  }

  newHall(){
    const dialogRef = this.dialog.open(HallFormComponent, {
      width: '450px',
    });
    dialogRef.afterClosed().subscribe(result => {
      this.delay(700).then(() => {
        this.ngOnInit();
      });
    });

  }

  editHall(hall: Hall){
    const dialogRef = this.dialog.open(HallFormComponent, {
      width: '450px',
      data: {
        hall: hall
      }
    });
    dialogRef.afterClosed().subscribe(result => {
      this.delay(700).then(() => {
        this.ngOnInit();
      });
    });

  }


  delay(ms: number) {
    return new Promise((resolve) => setTimeout(resolve, ms));
  }


  openDeleteDialog(hallUuid: String): void {
    const dialogRef = this.dialog.open(DeleteHallDialogComponent, {
      width: '450px',
      data: {
        hallUuid: hallUuid,
        borrado: false
      },
    });
    dialogRef.afterClosed().subscribe(result => {
      this.delay(700).then(() => {
        this.ngOnInit();
      });
      if(result?.borrado)
        this.toastr.success('Sala eliminada con éxito');
    });
  }

  public getPaginatorData(event: PageEvent): PageEvent {
    this.lowValue = event.pageIndex * event.pageSize;
    this.highValue = this.lowValue + event.pageSize;
    return event;
  }
}
