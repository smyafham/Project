import { Component } from '@angular/core';
import { MatButtonModule } from "@angular/material/button";
import { MatInputModule } from '@angular/material/input';
import { FormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { UserService } from '../../services/user.service';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-singnup',
  standalone: true,
  imports: [
    MatFormFieldModule,
    FormsModule,
    MatButtonModule,
    MatInputModule,
    MatCardModule, MatIconModule],
  providers: [],
  templateUrl: './singnup.component.html',
  styleUrl: './singnup.component.css'
})
export class SingnupComponent {
  hide: boolean = true;

  constructor(private userService: UserService) { }
  public user = {
    userName: '',
    password: '',
  }

  formSubmit() {
    console.log(this.user);
    if (this.user.userName == '' || this.user.userName == null) {
      alert('username is required!!');
      return;
    }
    //addUser:fromuserservice

    // .subscribe(
    //   (data)=>{
    //     //success
    //     console.log(data);
    //     alert('success');
    //     window.location.reload();
    //   },
    //   (error)=>{
    //       //error
    //       if (error.status === 200) {
    //         alert('success');
    //       }
    //       else{
    //       console.log(error);
    //       alert('error occured');
    //       window.location.reload();}
    //   }

    // )
    this.userService.addUser(this.user).toPromise()
      .then((data) => {
        // Success
        console.log(data);
        alert('Successfully created');
        window.location.reload();
        // Perform any necessary actions after successful user addition
      })
      .catch((error) => {
        // Error
        if (error.status === 200) {
          alert('User Registered Successfully');
          window.location.reload();
        }
        else {
          console.log(error);
          alert('User already exists');
          window.location.reload();
        }
        window.location.reload();
      }

      );
  }

}
