import { Component } from '@angular/core';
import { LoginService } from '../../../services/login.service';

@Component({
  selector: 'app-user-dashboard',
  standalone: true,
  imports: [],
  providers: [],
  templateUrl: './user-dashboard.component.html',
  styleUrl: './user-dashboard.component.css'
})
export class UserDashboardComponent {
  constructor(public login: LoginService) { }
  currentUserName: string = this.login.getName();

}
