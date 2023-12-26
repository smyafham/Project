import { Component, NgModule } from '@angular/core';
import { UserService } from '../../services/user.service';
import { CommonModule } from '@angular/common';
import { NgFor } from '@angular/common';
import { RouterModule } from '@angular/router';
import { LoginService } from '../../services/login.service';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import baseUrl from '../../services/helper';
import { FormsModule, NgModel } from '@angular/forms';

@Component({
  selector: 'app-users',
  standalone: true,
  imports: [CommonModule, NgFor, RouterModule, FormsModule],
  templateUrl: './users.component.html',
  styleUrl: './users.component.css',
  providers: []
})
export class UsersComponent {
  users: any[1000] = [];
  constructor(private service: UserService, private login: LoginService, private http: HttpClient) { }
  ngOnInit(): void {
    this.service.getUsers().subscribe(
      (data) => {
        this.users = data; // Assign the received data to the users array
      },
      (error) => {
        console.error(error);
        // Handle error if needed
      }
    );
  }

  token = this.login.getToken()
  Urll = 'http://localhost:8080';


  deleteUserById(userId: number) {
    const header2 = new HttpHeaders({
      Authorization: `Bearer ${this.token}`
    });

    const url = `${this.Urll}/user/delete/${userId}`;
    console.log(url); // Verify the constructed URL

    // this.http.delete(url, { headers: header2 }).subscribe(
    //   () => {
    //     console.log('User deleted successfully');
    //     alert('Row has been deleted');
    //     window.location.reload();
    //     // Perform any necessary actions after user deletion
    //   },
    //   error => {
    //     alert("Only SUPERADMINS can perform this operation");
    //     window.location.reload();
    //     // Handle error response or display an error message
    //   }
    // );
    this.http
      .delete(url, { headers: header2 })
      .toPromise()
      .then(() => {
        console.log('User deleted successfully');
        alert('Row has been deleted');
        window.location.reload();
        // Perform any necessary actions after user deletion
      })
      .catch((error) => {
        console.error('Error during deletion:', error);
        if (error.status === 403) {
          alert('Only SUPERADMINS can perform this operation');
        } else if (error.status === 200) {
          alert('Deleted successfully');
        }
        else {
          alert("error while deleting");
        }
        window.location.reload();
        // Handle error response or display an error message
      });
  }


  userId1: any // Track the ID of the user being updated
  userToUpdate: any = {}; // Object to store user details for updating
  isEditMode: boolean = false; // Flag to toggle edit mode


  originalUser: any = {};


  updateUser(value: any) {
    if(this.login.getUserRole()=='ADMIN'){
      alert('you should be a superAdmin to perform update!');
    }
    this.service.updateUser(value.id, value).subscribe(
      () => {
       
        alert('User updated successfully');
        console.log('User updated successfully');
        window.location.reload();
        // Perform any necessary actions after user update
      },
      error => {
        console.error('Error updating user:', error);
        // Handle error response or display an error message
      }
    );
  }
}
