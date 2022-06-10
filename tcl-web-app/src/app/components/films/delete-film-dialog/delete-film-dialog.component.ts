import { Subscription } from 'rxjs';
import { Component, Inject, OnInit, OnDestroy } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { FilmsService } from 'src/app/services/films.service';


export interface DialogData {
  filmUuid: String;
  filmTitle: String;
  borrado: boolean;
}

@Component({
  selector: 'app-delete-film-dialog',
  templateUrl: './delete-film-dialog.component.html',
  styleUrls: ['./delete-film-dialog.component.css']
})


export class DeleteFilmDialogComponent implements OnInit{


  constructor(
    public dialogRef: MatDialogRef<DeleteFilmDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData,
    private filmService: FilmsService
  ) { }


  ngOnInit(): void {
  }

  closeDialog(): void {
    this.dialogRef.close();
  }

  deleteFilm(): void {
    this.filmService.deleteFilm(this.data.filmUuid).subscribe();
    this.dialogRef.close({borrado:true});
  }

}
