import { Injectable } from '@angular/core';
import baseUrl from './helper';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { LoginService } from './login.service';
@Injectable({
  providedIn: 'root'
})

export class UserService {


  constructor(private http: HttpClient, private login: LoginService) { }

  token_a = this.login.getToken()


  public addUser(user: any) {
    return this.http.post(`${baseUrl}/user/create`, user)
  }

  public getUsers() {
    console.log(this.token_a);
    console.log("Hi");
    const header = new HttpHeaders({
      Authorization: `Bearer ${this.token_a}`
    });
    return this.http.get(`${baseUrl}/user/all`, { headers: header });

  }
  //getbyId

  getUserById(userId: number) {
    console.log("ingetbyid");
    const header = new HttpHeaders({
      Authorization: `Bearer ${this.token_a}`
    });
    return this.http.get(`${baseUrl}/user/${userId}`, { headers: header });

  }

  updateUser(userId: any, userData: any) {
    console.log("inupdate service")
    const header = new HttpHeaders({
      Authorization: `Bearer ${this.token_a}`
    });
    return this.http.put(`${baseUrl}/user/update/${userId}`, userData, { headers: header });
  }
  // public getUserById(id){
  //   return this.http.get("http://localhost:8080/user/"+id);

  //  }
  //  public updateById(id){
  //   return this.http.get("http://localhost:8080/user/update/"+id);

  //  }
  //  public deleteById(id){
  //   return this.http.get("http://localhost:8080/user/delete/"+id);

  //  }

}
