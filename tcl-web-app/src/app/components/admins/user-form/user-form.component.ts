import { EditUserDto } from './../../../models/dto/edit-user';
import { CreateUserDto } from './../../../models/dto/create-user';
import { Subscription } from 'rxjs';
import { Router } from '@angular/router';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { MAT_DATE_LOCALE } from '@angular/material/core';
import * as moment from 'moment';
import { User } from 'src/app/models/interfaces/user-response';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-user-form',
  templateUrl: './user-form.component.html',
  styleUrls: ['./user-form.component.css'],
})
export class UserFormComponent implements OnInit, OnDestroy {
  isEdit: boolean = false;
  user!: User;
  form!: FormGroup;
  imageSelected: boolean = false;
  imageFile!: File;
  selectedPreview!: string;
  subscriptions: Subscription[] = [];

  constructor(
    private router: Router,
    private formBuilder: FormBuilder,
    private toastr: ToastrService,
    private userService: UserService
  ) {
    if (this.router.getCurrentNavigation()!.extras.state) {
      this.user = this.router.getCurrentNavigation()!.extras!.state!['user']!;
      this.isEdit = true;
    }

    this.form = this.formBuilder.group({
      nombre: [null, [Validators.required]],
      nickName: [null, [Validators.required]],
      email: [null, [Validators.required]],
      fechaDeNacimiento: [null, [Validators.required]],
    });

    if (!this.isEdit) {
      this.form.addControl(
        'password',
        this.formBuilder.control(null, [Validators.required])
      );
    }

    if (this.isEdit) {

      this.form.addControl(
        'role',
        this.formBuilder.control(null, [Validators.required])
      );

      this.form.patchValue({
        nombre: this.user.nombre,
        nickName: this.user.nick,
        email: this.user.email,
        fechaDeNacimiento: this.user.fechaDeNacimiento,
        role: this.user.role
      });
    }
  }

  ngOnInit(): void {
    if (!this.isEdit && this.router.url.includes('edit-user')) {
      this.router.navigate(['/admins']);
    }
  }

  showPreview(file: File) {
    const reader = new FileReader();
    reader.onload = () => {
      this.selectedPreview = reader.result as string;
    };
    reader.readAsDataURL(file);
  }

  selectFile() {
    document.getElementById('fileSelector')!.click();
  }

  changeImageSelected(event: any) {
    if (event.target.files[0].type.includes('image')) {
      this.imageFile = event.target.files[0];
      this.showPreview(this.imageFile);
      this.imageSelected = true;
    } else {
      this.toastr.error(
        '(Formatos válidos ".jpg",".png")',
        'Formato no valido'
      );
    }
  }

  today() {
    return moment().format('YYYY-MM-DD');
  }

  editUser() {
    if (this.form.valid && this.imageSelected) {
      const editUser: EditUserDto = new EditUserDto(
        this.form.controls['nombre'].value,
        this.form.controls['nickName'].value,
        this.form.controls['email'].value,
        moment(
          this.form.controls['fechaDeNacimiento'].value.toString(),
          'ddd MMM DD YYYY HH:mm:ss'
        ).format('YYYY-MM-DD'),
        this.form.controls['role'].value
      );

      this.subscriptions.push(
        this.userService
          .editUser(editUser, this.imageFile, this.user.uuid)
          .subscribe((res: any) => {
            if (res.status === 200) {
              this.toastr.success('Usuario editado');
              this.router.navigate(['/admins']);
            }
          })
      );
    } else if (this.form.valid && !this.imageSelected) {
      const editUser: EditUserDto = new EditUserDto(
        this.form.controls['nombre'].value,
        this.form.controls['nickName'].value,
        this.form.controls['email'].value,
        moment(
          this.form.controls['fechaDeNacimiento'].value.toString(),
          'ddd MMM DD YYYY HH:mm:ss'
        ).format('YYYY-MM-DD'),
        this.form.controls['role'].value
      );
      this.subscriptions.push(
        this.userService
          .editUserNoPoster(editUser, this.user.uuid)
          .subscribe((res: any) => {
            if (res.status === 200) {
              this.toastr.success('Usuario editado');
              this.router.navigate(['/admins']);
            }
          })
      );
    } else {
      this.toastr.error('Por favor, rellene todos los campos', 'Error');
    }
  }

  createUser() {
    if (this.form.valid && this.imageSelected) {
      const newUser: CreateUserDto = new CreateUserDto(
        this.form.controls['nombre'].value,
        this.form.controls['nickName'].value,
        this.form.controls['email'].value,
        this.form.controls['password'].value,
        moment(
          this.form.controls['fechaDeNacimiento'].value.toString(),
          'ddd MMM DD YYYY HH:mm:ss'
        ).format('YYYY-MM-DD')
      );

      this.subscriptions.push(
        this.userService
          .createUserAdmin(newUser, this.imageFile)
          .subscribe((res: any) => {
            if (res.status === 201) {
              this.toastr.success('Usuario creado');
              this.router.navigate(['/admins']);
            }
          })
      );
    } else {
      this.toastr.error('Por favor, rellene todos los campos', 'Error');
    }
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach((sub) => sub.unsubscribe());
  }
}
