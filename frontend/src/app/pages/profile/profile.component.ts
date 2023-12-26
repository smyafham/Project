import { Component } from '@angular/core';
import { SidebarComponent } from '../admin/sidebar/sidebar.component';
import { MatCardModule } from '@angular/material/card';
import { LoginService } from '../../services/login.service';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [SidebarComponent, MatCardModule],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css'
})
export class ProfileComponent {
  constructor(private login: LoginService) { }
  name: string = this.login.getName();
  id = this.login.getID();
  active: boolean = this.login.getactive();
  role: string = this.login.getrole();
  status: string = 'ACTIVE';
}
