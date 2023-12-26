import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import baseUrl from './helper';

@Injectable({
  providedIn: 'root'
})
export class LoginService {



  constructor(private http: HttpClient) { }

  //current usr which is logged in
  public getCurrentUser() {
    console.log(this.getToken());
    const header = new HttpHeaders({
      Authorization: `Bearer ${this.getToken()}`
    });
    console.log("inside getcurrentuser");
    return this.http.get(`${baseUrl}/current-user`, { headers: header });
  }



  //generate token

  public generateToken(loginData: any) {
    console.log("in this")
    return this.http.post(`${baseUrl}/generate-token`, loginData);
  }

  //loginuser : set token in localstorage

  public loginUser(token: any) {
    localStorage.setItem('token', token);
    return true;
  }

  //user is logged in or not
  public isLoggedIn() {
    let tokenStr = localStorage.getItem('token');
    if (tokenStr == undefined || tokenStr == '' || tokenStr == null) {
      return false;
    } else {
      return true;
    }

  }

  //logout to remove token from local storage

  public logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('user');

    return true;
  }

  //get token
  public getToken() {
    return localStorage.getItem('token');
  }

  //set user details
  public setUser(user: any) {
    localStorage.setItem('user', JSON.stringify(user));

  }

  //get user
  public getUser() {
    let userStr = localStorage.getItem('user');
    if (userStr != null) {
      return JSON.parse(userStr);
    } else {
      this.logout();
      return null;
    }
  }

  //get user role
  public getUserRole() {
    let user = this.getUser();
    return user.roles;
  }
  public getName() {
    let user = this.getUser();
    return user.userName;
  }
  public getID() {
    let user = this.getUser();
    return user.id;
  }
  public getactive() {
    let user = this.getUser();
    return user.isActive;
  }
  public getrole() {
    let user = this.getUser();
    return user.roles;
  }


}