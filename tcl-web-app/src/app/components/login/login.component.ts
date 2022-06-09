import { AuthService } from './../../services/auth.service';
import { Component, OnDestroy, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  NgModel,
  Validators,
} from '@angular/forms';
import { Subscription } from 'rxjs';
import { NavigationExtras, Router } from '@angular/router';
import { LoginDto } from 'src/app/models/dto/login';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent implements OnInit, OnDestroy {
  hide = true;

  form!: FormGroup;
  private subscriptions: Subscription[] = [];
  loginDto!: LoginDto;

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    this.form = this.formBuilder.group({
      nickname: [null, [Validators.required]],
      password: [null, [Validators.required]],
    });
  }

  onSubmit() {
    this.loginDto = new LoginDto(
      this.form.controls['nickname'].value,
      this.form.controls['password'].value
    );
    this.subscriptions.push(
      this.authService.login(this.loginDto).subscribe((authState) => {
          if(authState.role == 'ADMIN'){
            localStorage.setItem('tcl-token', authState.token);
            localStorage.setItem('tcl-avatar', authState.avatar);
            localStorage.setItem('tcl-nick', authState.nickname);
            this.router.navigate(['/home']);
          }else{
            this.toastr.error("No tiene permiso para acceder a esta sección.","Acceso no autorizado");

          }
        },(error) => {
          this.toastr.error("Credenciales incorrectas","Error al iniciar sesión");
      })
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach((s) => s.unsubscribe());
  }
}
