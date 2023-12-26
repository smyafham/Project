import { CanActivateFn, Router } from '@angular/router';
import { LoginService } from './login.service';

export const adminGuard: CanActivateFn = (route, state) => {

  constructor(private login: LoginService, private router: Router) {}

  return true;
};
