import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { LoginService } from './login.service';

describe('LoginService', () => {
  let service: LoginService;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [LoginService]
    });

    service = TestBed.inject(LoginService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTestingController.verify();
    localStorage.clear(); // Ensure localStorage is cleared after each test
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should get current user with authorization header', () => {
    const mockToken = 'mockToken';
    spyOn(service, 'getToken').and.returnValue(mockToken);

    service.getCurrentUser().subscribe();
    const req = httpTestingController.expectOne(`${service['baseUrl']}/user/create`);

    expect(req.request.method).toEqual('GET');
    expect(req.request.headers.get('Authorization')).toEqual(`Bearer ${mockToken}`);

    req.flush({});
  });

  it('should generate token with login data', () => {
    const mockLoginData = { username: 'test', password: 'password' };

    service.generateToken(mockLoginData).subscribe();

    const req = httpTestingController.expectOne(`${service['baseUrl']}/generate-token`);
    expect(req.request.method).toEqual('POST');
    expect(req.request.body).toEqual(mockLoginData);

    req.flush({});
  });

  it('should set and retrieve user details', () => {
    const mockUser = { id: 1, userName: 'testUser', roles: ['user'], isActive: true };

    service.setUser(mockUser);

    const retrievedUser = service.getUser();
    expect(retrievedUser).toEqual(mockUser);
  });

  it('should remove token and user details on logout', () => {
    spyOn(localStorage, 'removeItem').and.callThrough();

    service.logout();

    expect(localStorage.removeItem).toHaveBeenCalledWith('token');
    expect(localStorage.removeItem).toHaveBeenCalledWith('user');
  });

  it('should return false for isLoggedIn when token is not present', () => {
    expect(service.isLoggedIn()).toBeFalsy();
  });

  it('should return true for isLoggedIn when token is present', () => {
    spyOn(localStorage, 'getItem').and.returnValue('mockToken');

    expect(service.isLoggedIn()).toBeTruthy();
  });

  it('should return user role', () => {
    const mockUser = { roles: ['admin'] };
    spyOn(service, 'getUser').and.returnValue(mockUser);

    const userRole = service.getUserRole();

    expect(userRole).toEqual(mockUser.roles);
  });

  it('should return user name', () => {
    const mockUser = { userName: 'testUser' };
    spyOn(service, 'getUser').and.returnValue(mockUser);

    const userName = service.getName();

    expect(userName).toEqual(mockUser.userName);
  });

  it('should return user ID', () => {
    const mockUser = { id: 123 };
    spyOn(service, 'getUser').and.returnValue(mockUser);

    const userId = service.getID();

    expect(userId).toEqual(mockUser.id);
  });

  it('should return user activity status', () => {
    const mockUser = { isActive: true };
    spyOn(service, 'getUser').and.returnValue(mockUser);

    const isActive = service.getactive();

    expect(isActive).toEqual(mockUser.isActive);
  });

  it('should return user roles', () => {
    const mockUser = { roles: ['user', 'admin'] };
    spyOn(service, 'getUser').and.returnValue(mockUser);

    const userRoles = service.getrole();

    expect(userRoles).toEqual(mockUser.roles);
  });

  // Add more test cases based on your service's functionality
});
