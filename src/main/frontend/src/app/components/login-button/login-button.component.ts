import { Component, OnInit } from '@angular/core';
import { UserService } from '../../service/user.service'

@Component({
  selector: 'app-login-button',
  templateUrl: './login-button.component.html',
  styleUrls: ['./login-button.component.css']
})
export class LoginButtonComponent implements OnInit {

  loginStatus: boolean;

  constructor(private user: UserService) {
    this.user.loginStatus.subscribe(res => this.loginStatus = res)
  }

  ngOnInit(): void {
  }

  login() {
    this.user.login()
  }

  logout() {
    this.user.logout()
  }

  getUser() {
    return sessionStorage.getItem('userId');
  }

  getName() {
    return sessionStorage.getItem('userName');
  }

  getUserImg() {
    return sessionStorage.getItem("userImg")
  }

}
