import { SessionsService } from 'src/app/services/sessions.service';
import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

export interface DialogData {
  filmUuid: String;
  borrado: boolean;
}

@Component({
  selector: 'app-delete-sesssion-dialog',
  templateUrl: './delete-sesssion-dialog.component.html',
  styleUrls: ['./delete-sesssion-dialog.component.css']
})
export class DeleteSessionDialogComponent implements OnInit {


  constructor(
    public dialogRef: MatDialogRef<DeleteSessionDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData,
    private SessionsService: SessionsService
  ) { }


  ngOnInit(): void {
  }

  closeDialog(): void {
    this.dialogRef.close();
  }

  deleteSession(): void {
    this.SessionsService.deleteSession(this.data.filmUuid).subscribe();
    this.dialogRef.close({borrado:true});
  }

}
