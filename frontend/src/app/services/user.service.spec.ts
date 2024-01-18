import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { UserService } from './user.service';
import { LoginService } from './login.service';

describe('UserService', () => {
  let service: UserService;
  let httpTestingController: HttpTestingController;
  let loginServiceSpy: jasmine.SpyObj<LoginService>;

  beforeEach(() => {
    const spy = jasmine.createSpyObj('LoginService', ['getToken']);

    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        UserService,
        { provide: LoginService, useValue: spy }
      ]
    });

    service = TestBed.inject(UserService);
    httpTestingController = TestBed.inject(HttpTestingController);
    loginServiceSpy = TestBed.inject(LoginService) as jasmine.SpyObj<LoginService>;
    //const loginServiceSpy = jasmine.createSpyObj('LoginService', ['getToken']);

  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should add a user', () => {
    const userId = 1;
    const user = {
      id: userId,
      username: 'newUsername',
      password: 'newPassword',
      active: 'true',
      roles:'USER'
    };

    service.addUser(user).subscribe();

    const req = httpTestingController.expectOne(`${service['baseUrl']}/user/create`);
    expect(req.request.method).toBe('POST');
    req.flush({});
  });

  it('should get users', () => {
    loginServiceSpy.getToken.and.returnValue('fakeToken');

    service.getUsers().subscribe();

    const req = httpTestingController.expectOne(`${service['baseUrl']}/user/all`);
    expect(req.request.method).toBe('GET');
    //expect(req.request.headers.get('Authorization')).toBe('Bearer fakeToken');
    req.flush({});
  });

  it('should get user by id', () => {
    loginServiceSpy.getToken.and.returnValue('fakeToken');
    const userId = 1;

    service.getUserById(userId).subscribe();

    const req = httpTestingController.expectOne(`${service['baseUrl']}/user/${userId}`);
    expect(req.request.method).toBe('GET');
   // expect(req.request.headers.get('Authorization')).toBe('Bearer fakeToken');
    req.flush({});
  });

  it('should update user', () => {
    loginServiceSpy.getToken.and.returnValue('fakeToken');
    const userId = 1;
    const userData = {
      id: userId,
      username: 'newUsername',
      password: 'newPassword',
      active: 'true',
      roles:'USER'
    };

    service.updateUser(userId, userData).subscribe();

    const req = httpTestingController.expectOne(`${service['baseUrl']}/user/update/${userId}`);
    expect(req.request.method).toBe('PUT');
    //expect(req.request.headers.get('Authorization')).toBe('Bearer fakeToken');
    req.flush({});
  });

  it('should handle errors when adding a user', () => {
    const userId = 1;
    const user = {
      id: userId,
      username: 'newUsername',
      password: 'newPassword',
      active: 'true',
      roles:'USER'
    };

    service.addUser(user).subscribe(
      () => fail('Failed with an error'),
      (error) => {
        expect(error.status).toBe(500); 
      }
    );

    const req = httpTestingController.expectOne(`${service['baseUrl']}/user/create`);
    expect(req.request.method).toBe('POST');
    req.flush({}, { status: 500, statusText: 'Internal Server Error' });
  });

});
