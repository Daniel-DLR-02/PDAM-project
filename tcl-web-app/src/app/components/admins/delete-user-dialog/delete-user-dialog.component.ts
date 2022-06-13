import { Subscription } from 'rxjs';
import { Component, Inject, OnInit, OnDestroy } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { UserService } from 'src/app/services/user.service';


export interface DialogData {
  userUuid: String;
  userNick: String;
  borrado: boolean;
}

@Component({
  selector: 'app-delete-user-dialog',
  templateUrl: './delete-user-dialog.component.html',
  styleUrls: ['./delete-user-dialog.component.css']
})


export class DeleteUserDialogComponent implements OnInit{


  constructor(
    public dialogRef: MatDialogRef<DeleteUserDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData,
    private userService: UserService
  ) { }


  ngOnInit(): void {
  }

  closeDialog(): void {
    this.dialogRef.close();
  }

  deleteUser(): void {
    this.userService.deleteUser(this.data.userUuid).subscribe();
    this.dialogRef.close({borrado:true});
  }

}
