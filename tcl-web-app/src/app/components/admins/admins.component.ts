import { DeleteUserDialogComponent } from './delete-user-dialog/delete-user-dialog.component';
import { Subscription } from 'rxjs';
import { Router, NavigationExtras } from '@angular/router';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { PageEvent } from '@angular/material/paginator';
import { MatDialog } from '@angular/material/dialog';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-admins',
  templateUrl: './admins.component.html',
  styleUrls: ['./admins.component.css'],
})
export class AdminsComponent implements OnInit, OnDestroy {
  users: User[] = [];
  subscriptions: Subscription[] = [];
  lowValue = 0;
  highValue = 5;
  filterSearch!: String;
  moviesToView: any[] = [];

  constructor(
    private router: Router,
    private usersService: UsersService,
    public dialog: MatDialog,
    private toastr: ToastrService
  ) {}

  ngOnInit() {
    this.filterSearch = '';
    this.subscriptions.push(
      this.usersService.getUsers().subscribe((users) => {
        this.users = users.content;
        this.moviesToView = this.users;
      })
    );
  }

  newUser() {
    this.router.navigateByUrl('/user/new-user');
  }

  editUser(user: User) {
    const extras: NavigationExtras = {
      state: {
        user: user,
      },
    };
    this.router.navigate(['/user/edit-user'], extras);
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach((subscription) => subscription.unsubscribe());
  }

  public getPaginatorData(event: PageEvent): PageEvent {
    this.lowValue = event.pageIndex * event.pageSize;
    this.highValue = this.lowValue + event.pageSize;
    return event;
  }

  filterUser() {
    this.moviesToView = [];

    if (!(this.filterSearch == '')) {
      this.users.forEach((f) => {
        if (
          f['nickname'].toLowerCase().includes(this.filterSearch.toLowerCase()) &&
          !this.moviesToView.includes(f)
        ) {
          this.moviesToView.push(f);
        }
      });
    } else if (
      this.filterSearch === '' ||
      this.filterSearch === '<empty string>'
    ) {
      this.moviesToView = this.users;
    }
  }

  delay(ms: number) {
    return new Promise((resolve) => setTimeout(resolve, ms));
  }

  openDeleteDialog(userUuid: String, userTitle: String): void {
    var borrado: boolean;
    const dialogRef = this.dialog.open(DeleteUserDialogComponent, {
      width: '450px',
      data: {
        userUuid: userUuid,
        userTitle: userTitle,
        borrado: false
      },
    });
    dialogRef.afterClosed().subscribe(result => {
      this.delay(700).then(() => {
        this.ngOnInit();
      });
      if(result?.borrado)
        this.toastr.success('Película eliminada con éxito');
    });
  }
}
