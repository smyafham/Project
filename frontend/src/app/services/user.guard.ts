import { Injectable } from '@angular/core';
import { CanActivate, Router, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
}
)

export class UserGuard implements CanActivate {

  constructor(private router: Router) { }
  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    console.log('in user guard');
    return this.userHasPermission();
  }

  private userHasPermission(): Observable<boolean | UrlTree> | boolean | UrlTree {
    const token = localStorage.getItem('token');
    console.log(token);
    let use = localStorage.getItem('user');
    if (use != null && token != null) {
      let user = JSON.parse(use);
      console.log(user);

      if (user.roles == 'USER') {
        return true;
      }
      else {
        alert('must be user');
        this.router.navigate(['admins']);
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