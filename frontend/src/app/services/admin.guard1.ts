// admin.guard.ts

import { Injectable, forwardRef } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { LoginService } from './login.service';

@Injectable({
  providedIn: 'root'
})
export class AdminGuard implements CanActivate {

  constructor(private login: LoginService, private router: Router) { }

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot) {
    return this.checkUserRole();
  }

  private checkUserRole() {
    if (this.userHasPermission()) {
      return true;
    }
    this.router.navigate(['login']);
    return false;
  }

  private userHasPermission() {
    return (this.login.isLoggedIn() && (this.login.getUserRole() == 'SUPERADMIN' || this.login.getUserRole() == 'ADMIN'));
  }
}

