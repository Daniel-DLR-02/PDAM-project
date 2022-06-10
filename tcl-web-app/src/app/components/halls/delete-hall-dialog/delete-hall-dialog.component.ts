import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { HallsService } from 'src/app/services/halls.service';

export interface DialogData {
  filmUuid: String;
  borrado: boolean;
}

@Component({
  selector: 'app-delete-hall-dialog',
  templateUrl: './delete-hall-dialog.component.html',
  styleUrls: ['./delete-hall-dialog.component.css']
})
export class DeleteHallDialogComponent implements OnInit {

  constructor(
    public dialogRef: MatDialogRef<DeleteHallDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData,
    private hallsService: HallsService
  ) { }


  ngOnInit(): void {
  }

  closeDialog(): void {
    this.dialogRef.close();
  }

  deleteSession(): void {
    this.hallsService.deleteHall(this.data.filmUuid).subscribe();
    this.dialogRef.close({borrado:true});
  }
}
