import { Component } from '@angular/core';
import { AdminGuard } from '../../../services/admin.guard';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { LoginService } from '../../../services/login.service';
import { RouterModule } from '@angular/router';
import { FormsModule, NgModel } from '@angular/forms';
import { NgFor, NgIf } from '@angular/common';
import { UserService } from '../../../services/user.service';
import { NavbarComponent } from '../../../components/navbar/navbar.component';
import { SidebarComponent } from '../sidebar/sidebar.component';
import { WelcomeComponent } from '../welcome/welcome.component';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [RouterModule, FormsModule, NgIf, NgFor, SidebarComponent, WelcomeComponent],
  providers: [],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent {
  constructor(public login: LoginService, public service: UserService) { }
  currentUserName: string = this.login.getName();

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
