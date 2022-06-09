import { CreateFilmDto } from './../../../models/dto/create-film';
import { Subscription } from 'rxjs';
import { Film } from './../../../models/interfaces/films-response';
import { Router } from '@angular/router';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { MAT_DATE_LOCALE } from '@angular/material/core';
import { FilmsService } from 'src/app/services/films.service';
import * as moment from 'moment';

@Component({
  selector: 'app-films-form',
  templateUrl: './films-form.component.html',
  styleUrls: ['./films-form.component.css'],
})
export class FilmsFormComponent implements OnInit, OnDestroy {
  isEdit: boolean = false;
  film!: Film;
  form!: FormGroup;
  imageSelected: boolean = false;
  imageFile!: File;
  selectedPreview!: string;
  subscriptions: Subscription[] = [];

  constructor(
    private router: Router,
    private formBuilder: FormBuilder,
    private toastr: ToastrService,
    private filmsService: FilmsService
  ) {
    if (this.router.getCurrentNavigation()!.extras.state) {
      this.film = this.router.getCurrentNavigation()!.extras!.state!['film']!;
      this.isEdit = true;
    }

    this.form = this.formBuilder.group({
      title: [null, [Validators.required]],
      description: [null, [Validators.required]],
      duration: [null, [Validators.required]],
      releaseDate: [null, [Validators.required]],
      expirationDate: [null, [Validators.required]],
      genre: [null, [Validators.required]],
    });

    if (this.isEdit) {
      this.form.patchValue({
        title: this.film.title,
        description: this.film.description,
        duration: this.film.duration,
        releaseDate: this.film.releaseDate,
        expirationDate: this.film.expirationDate,
        genre: this.film.genre,
      });
    }
  }

  ngOnInit(): void {
    if (!this.isEdit && this.router.url.includes('edit-film')) {
      this.router.navigate(['/films']);
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

  editFilm() {
    if (this.form.valid && this.imageSelected) {
      const editFilm: CreateFilmDto = new CreateFilmDto(
        this.form.controls['title'].value,
        this.form.controls['description'].value,
        this.form.controls['duration'].value,
        moment(
          this.form.controls['releaseDate'].value.toString(),
          'ddd MMM DD YYYY HH:mm:ss'
        ).format('YYYY-MM-DD'),
        moment(
          this.form.controls['expirationDate'].value.toString(),
          'ddd MMM DD YYYY HH:mm:ss'
        ).format('YYYY-MM-DD'),
        this.form.controls['genre'].value
      );

      this.subscriptions.push(
        this.filmsService
          .editFilm(editFilm, this.imageFile, this.film.uuid)
          .subscribe((res: any) => {
            if (res.status === 200) {
              this.toastr.success('Película editada');
              this.router.navigate(['/films']);
            }
          })
      );
    } else if (this.form.valid && !this.imageSelected) {
      const editFilm: CreateFilmDto = new CreateFilmDto(
        this.form.controls['title'].value,
        this.form.controls['description'].value,
        this.form.controls['duration'].value,
        moment(
          this.form.controls['releaseDate'].value.toString(),
          'ddd MMM DD YYYY HH:mm:ss'
        ).format('YYYY-MM-DD'),
        moment(
          this.form.controls['expirationDate'].value.toString(),
          'ddd MMM DD YYYY HH:mm:ss'
        ).format('YYYY-MM-DD'),
        this.form.controls['genre'].value
      );
      this.subscriptions.push(
        this.filmsService
          .editFilmNoPoster(editFilm, this.film.uuid)
          .subscribe((res: any) => {
            if (res.status === 200) {
              this.toastr.success('Película editada');
              this.router.navigate(['/films']);
            }
          })
      );
    } else {
      this.toastr.error('Por favor, rellene todos los campos', 'Error');
    }
  }

  createFilm() {
    if (this.form.valid && this.imageSelected) {
      const newFilm: CreateFilmDto = new CreateFilmDto(
        this.form.controls['title'].value,
        this.form.controls['description'].value,
        this.form.controls['duration'].value,
        moment(
          this.form.controls['releaseDate'].value.toString(),
          'ddd MMM DD YYYY HH:mm:ss'
        ).format('YYYY-MM-DD'),
        moment(
          this.form.controls['expirationDate'].value.toString(),
          'ddd MMM DD YYYY HH:mm:ss'
        ).format('YYYY-MM-DD'),
        this.form.controls['genre'].value
      );

      this.subscriptions.push(
        this.filmsService
          .createFilm(newFilm, this.imageFile)
          .subscribe((res: any) => {
            if (res.status === 201) {
              this.toastr.success('Película creada');
              this.router.navigate(['/films']);
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
