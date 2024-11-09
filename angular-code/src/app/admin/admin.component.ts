import {Component, OnInit, ViewChild} from '@angular/core';
import {FormsModule, NgForm} from '@angular/forms';
import {DatePipe, NgClass, NgForOf, NgIf} from '@angular/common';
import { MatDialog } from '@angular/material/dialog';
import { AdminService } from './service/admin.service';
import { UserDTO } from './model/user.model';
import { ConfirmationDialogComponent } from "../dashboard/job-management/confirmation-dialog/confirmation-dialog.component";

@Component({
  selector: 'app-admin',
  standalone: true,
  templateUrl: './admin.component.html',
  imports: [FormsModule, NgClass, NgIf, NgForOf, DatePipe],
  styleUrls: ['./admin.component.scss']
})
export class AdminComponent implements OnInit {
  @ViewChild('userForm') userForm!: NgForm;
  users: UserDTO[] = [];
  filteredUsers: UserDTO[] = [];
  showAddUserModal = false;
  newUser: UserDTO = { login: '', email: '', position: '', firstName: '', lastName: '', dateOfBirth: '', locked: true };

  constructor(private adminService: AdminService, private dialog: MatDialog) {}

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers() {
    this.adminService.getAllUsers().subscribe(users => {
      this.users = users;
      this.filteredUsers = [...this.users];
    });
  }

  openAddUserModal() {
    this.showAddUserModal = true;
  }

  closeAddUserModal() {
    this.showAddUserModal = false;
  }

  addUser() {
    if (this.userForm.invalid) {
      this.userForm.form.markAllAsTouched();
      return;
    }

    this.adminService.addUser(this.newUser).subscribe(user => {
      this.users.push(user);
      this.filterUsers();
      this.closeAddUserModal();
    });
  }

  toggleUserStatus(user: UserDTO) {
    const dialogRef = this.dialog.open(ConfirmationDialogComponent, {
      data: `Czy na pewno chcesz ${user.locked ? 'odblokować' : 'zablokować'} tego użytkownika?`
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        const newStatus = !user.locked;
        this.adminService.toggleUserStatus(user.id!, newStatus).subscribe(() => {
          user.locked = newStatus;
          this.filterUsers();
        });
      }
    });
  }

  removeUser(userId: number) {
    const dialogRef = this.dialog.open(ConfirmationDialogComponent, {
      data: 'Czy na pewno chcesz usunąć tego użytkownika?'
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.adminService.deleteUser(userId).subscribe(() => {
          this.users = this.users.filter(user => user.id !== userId);
          this.filterUsers();
        });
      }
    });
  }

  onSearch(event: Event) {
    const query = (event.target as HTMLInputElement).value.toLowerCase();
    this.filteredUsers = this.users.filter(user =>
      user.login.toLowerCase().includes(query) || user.email.toLowerCase().includes(query)
    );
  }

  onStatusFilterChange(event: Event) {
    const selectedValue = (event.target as HTMLSelectElement).value;
    this.filteredUsers = this.users.filter(user =>
      selectedValue === '' || (selectedValue === 'active' ? !user.locked : user.locked)
    );
  }

  filterUsers() {
    this.filteredUsers = [...this.users];
  }
}
