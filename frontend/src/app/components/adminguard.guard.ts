import { CanActivateFn } from '@angular/router';

export const adminguardGuard: CanActivateFn = (route, state) => {
  return true;
};
