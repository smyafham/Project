import { Injectable } from '@angular/core';
import { CanActivate, Router, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { LoginService } from './login.service';

@Injectable({
  providedIn: 'root',
}
)

export class AdminGuard implements CanActivate {

  constructor(private router: Router) { }
  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    console.log('in admin guard');
    return this.userHasPermission();
  }


  private userHasPermission(): Observable<boolean | UrlTree> | boolean | UrlTree {
    const token = localStorage.getItem('token');
    console.log(token);
    let use = localStorage.getItem('user');
    if (use != null && token != null) {
      let user = JSON.parse(use);
      console.log(user);

      if (user.roles == 'SUPERADMIN' || user.roles == 'ADMIN') {
        return true;
      }
      else if (user.roles == 'USER') {
        console.log('userrole')
        alert('You need to be a SUPERADMIN or ADMIN to access this url');
        this.router.navigate(['user-dashboard']);
        return false;
      }
    }
    this.router.navigate(['login']);
    return false;

  }
  //   console.log('in admin guard userpermission');

  //   return this.login.isLoggedIn() && (this.login.getUserRole() === 'SUPERADMIN' || this.login.getUserRole() === 'ADMIN');
  // }

}
