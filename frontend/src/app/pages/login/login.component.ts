import { Component, OnInit } from '@angular/core';
import { MatButtonModule } from "@angular/material/button";
import { MatInputModule } from '@angular/material/input';
import { FormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatCardModule } from '@angular/material/card';
import { MatSnackBar } from '@angular/material/snack-bar';
import { LoginService } from '../../services/login.service';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [MatButtonModule,
    MatCardModule, MatInputModule, FormsModule, MatFormFieldModule, MatIconModule],
  providers: [LoginService, HttpClient, HttpClientModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {

  hide: boolean = true;

  constructor(private snack: MatSnackBar, private login: LoginService) { }
  public loginData = {
    username: '',
    password: '',
  };
  formSubmit() {
    console.log('login button clicked');
    if (this.loginData.username == '' || this.loginData.username == null) {
      this.snack.open('username is rquired !!', '', {
        duration: 3000,
      });
      return;

    }

    if (this.loginData.password == '' || this.loginData.password == null) {
      this.snack.open('password is rquired !!', '', {
        duration: 3000,
      });
      return;

    }
    //request to server to generate token
    console.log(this.loginData.username + this.loginData.password + "44");
    this.login.generateToken(this.loginData).subscribe(
      (data: any) => {
        console.log("success");
        console.log(data);

        //login...
        this.login.loginUser(data.token);
        console.log(data.token + "line  51");
        this.login.getCurrentUser().subscribe(
          (user: any) => {
            this.login.setUser(user);
            console.log(user);
            console.log("in loginuser");
            //redirect...admin:admin-dashboard


            //redirect...user:user-dashboard
            if (this.login.getUserRole() === "SUPERADMIN" || this.login.getUserRole() === "ADMIN") {
              window.location.href = '/admins';
            } else if (this.login.getUserRole() === "USER") {
              window.location.href = '/user-dashboard';
            } else {
              this.login.logout();
            }
          }
        );

      },
      (error) => {
        console.log("error!");
        console.log(error);
        this.snack.open("Invalid Details !! Try again", '', {
          duration: 3000,
        });
      }
    )

  }




}
