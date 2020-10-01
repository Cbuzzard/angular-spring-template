import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { UrlService } from './url.service';
import { Subject } from 'rxjs';

declare var gapi: any;


@Injectable({
  providedIn: 'root'
})
export class UserService {


  gapiSetup: boolean = false;
  loginStatus: Subject<boolean> = new Subject<boolean>();
  authInstance: gapi.auth2.GoogleAuth;
  userId;

  constructor(private http: HttpClient, private url: UrlService) {
    this.initGoogleAuth();
    this.refreshStatus();
  }

  refreshStatus() {
    this.authenticate().then(res => this.loginStatus.next(JSON.stringify(res) === 'true'))
  }

  async authenticate(): Promise<Object> {
    const httpOptions = { headers: new HttpHeaders({}) };
    return await this.http.get(`${this.url.backend}/rest/authenticate`, httpOptions).toPromise();
  }

  login() {
    return this.authInstance.signIn().then(user => {
      var xhr = new XMLHttpRequest();
      xhr.open('POST', `${this.url.backend}/login`);
      xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
      xhr.setRequestHeader('X-ID-TOKEN', user.getAuthResponse().id_token);
      xhr.send();
      xhr.onload = async () => {
        sessionStorage.setItem('token', xhr.getResponseHeader('Authorization'))
        sessionStorage.setItem('userId', user.getId())
        sessionStorage.setItem('userName', user.getBasicProfile().getName())
        sessionStorage.setItem('userImg', user.getBasicProfile().getImageUrl())
        window.location.reload();
      };
    })
  }

  logout() {
    gapi.auth2.getAuthInstance().signOut().then(async () => {
      sessionStorage.clear();
      this.refreshStatus()
      window.location.reload();
    })
  }

  async initGoogleAuth() {
    if (!this.gapiSetup) {
      const pload = new Promise((resolve) => {
        gapi.load('auth2', resolve);
      });
  
      return pload.then(async () => {
        gapi.auth2
          .init({ client_id: '428759163089-0un7vcmrckisnvpt06hqr6v1orbmmap0.apps.googleusercontent.com' })
          .then(auth => {
            this.gapiSetup = true;
            this.authInstance = auth;
          });
        });
      }
    }
}
