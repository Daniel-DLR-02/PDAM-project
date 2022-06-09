import { SessionsService } from 'src/app/services/sessions.service';
import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

export interface DialogData {
  sessionUuid: String;
  borrado: boolean;
}

@Component({
  selector: 'app-delete-sesssion-dialog',
  templateUrl: './delete-sesssion-dialog.component.html',
  styleUrls: ['./delete-sesssion-dialog.component.css']
})
export class DeleteSesssionDialogComponent implements OnInit {


  constructor(
    public dialogRef: MatDialogRef<DeleteSesssionDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData,
    private sessionsService: SessionsService
  ) { }


  ngOnInit(): void {
  }

  closeDialog(): void {
    this.dialogRef.close();
  }

  deleteSession(): void {
    this.sessionsService.deleteSession(this.data.sessionUuid).subscribe();
    this.dialogRef.close({borrado:true});
  }

}
