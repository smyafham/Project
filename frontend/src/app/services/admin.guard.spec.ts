import { TestBed } from '@angular/core/testing';
import { CanActivateFn } from '@angular/router';

import { AdminGuard } from './admin.guard';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { LoginService } from './login.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('AdminGuard', () => {
  let guard: AdminGuard;
  
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[HttpClientTestingModule],
      providers: [AdminGuard,LoginService,HttpClientModule,HttpClient]
    });
    guard = TestBed.inject(AdminGuard);
  });

  it('should be created', () => {
    expect(guard).toBeTruthy();
  });
});