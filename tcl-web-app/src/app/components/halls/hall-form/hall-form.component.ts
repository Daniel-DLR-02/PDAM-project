import { Router } from '@angular/router';
import { Component, Inject, OnInit, OnDestroy } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Subscription } from 'rxjs';
import { Hall } from 'src/app/models/interfaces/halls-response';
import { HallsService } from 'src/app/services/halls.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { CreateHallDto } from 'src/app/models/dto/create-hall';


export interface DialogData {
  success: boolean,
  hall: Hall
}


@Component({
  selector: 'app-hall-form',
  templateUrl: './hall-form.component.html',
  styleUrls: ['./hall-form.component.css']
})
export class HallFormComponent implements OnInit {


  constructor(
    public dialogRef: MatDialogRef<HallFormComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData,
    private hallService: HallsService,
    private toastr: ToastrService,
    private router: Router,
    private formBuilder: FormBuilder,
  ) {
  }

  subscriptions: Subscription[] = [];
  isEdit: boolean=false;
  form!: FormGroup;

  ngOnInit(): void {

    this.form = this.formBuilder.group({
      name: [null, [Validators.required]]
    });

    if(this.data.hall){
      this.isEdit=true;
      this.form.patchValue({
        name: this.data.hall.name,
      });
    }

  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(s => s.unsubscribe());
  }

  closeDialog(): void {
    this.dialogRef.close();
  }

  createHall() {


    if (this.form.valid) {
      const newHall: CreateHallDto = new CreateHallDto(
        this.form.controls['name'].value
      );

      this.subscriptions.push(
        this.hallService
          .createHall(newHall)
          .subscribe((res: Hall) => {
            if (res) {
              this.toastr.success('Sala creada');
              this.closeDialog();
            }
          })
      );
    }
    else {
      this.toastr.error('Por favor, rellene todos los campos', 'Error');
    }
  }



  editHall() {


    if (this.form.valid) {
      const editHall: CreateHallDto = new CreateHallDto(
        this.form.controls['name'].value
      );

      this.subscriptions.push(
        this.hallService
          .editHall(editHall,this.data.hall.uuid)
          .subscribe((res: Hall) => {
            if (res) {
              this.toastr.success('Sala editada');
              this.closeDialog();
            }
          })
      );
    }
    else {
      this.toastr.error('Por favor, rellene todos los campos', 'Error');
    }
  }
}
