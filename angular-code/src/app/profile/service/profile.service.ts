import { Injectable } from '@angular/core';
import {environment} from "../../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {User} from "../model/user";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root',
})
export class ProfileService {

  private readonly profileUrl = environment.api + 'profile';

  constructor(private http: HttpClient) {
  }

  getProfileData(): Observable<User> {
    return this.http.get<User>(this.profileUrl);
  }

  updateProfileData(changeProfileData: User): Observable<User> {
    return this.http.post<User>(this.profileUrl, changeProfileData);
  }
}
