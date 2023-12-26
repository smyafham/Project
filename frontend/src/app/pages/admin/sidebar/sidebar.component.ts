import { HttpErrorResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import { MatButtonModule, MatIconButton } from '@angular/material/button';
import { MatCard, MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import { RouterModule } from '@angular/router';
import { LoginService } from '../../../services/login.service';
import { UserService } from '../../../services/user.service';
import { FormsModule, NgModel } from '@angular/forms';
import { NgIf } from '@angular/common';


@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [MatIconModule, MatListModule, MatCardModule, MatButtonModule, RouterModule, FormsModule, NgIf],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.css'
})
export class SidebarComponent {
  constructor(public login: LoginService, public service: UserService) { }
  showSearchField: boolean = false;
  userId: any = '';
  userData: any = null;
  toggleSearchField(): void {
    this.showSearchField = !this.showSearchField;
  }
  getUserDetails(userId: number | null) {
    if (!userId) {
      // If userId is null or undefined, display an alert
      alert('Please enter a valid User ID');
      return; // Exit the method
    }

    this.service.getUserById(userId).subscribe(
      (userData: any) => {
        console.log(userData);
        this.userData = userData;
      },
      (error: HttpErrorResponse) => {
        if (error.status === 404) {
          alert('User not found'); // Display alert for 404 error
        } else if (error.status === 403) {
          console.error('Error fetching user details:', error);
          alert('Only SuperAdmins can access this');
          // Handle other error responses or display an error message
        }
        else {
          console.error('Error fetching user details:', error);
          // Handle other error responses or display an error message
        }
      }
    );
  }
}
