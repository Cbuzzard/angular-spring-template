import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class UrlService {

  constructor() { }

  //Prod
  // backend: string = ""
  //Dev
  backend: string = "http://localhost:5000"

}
