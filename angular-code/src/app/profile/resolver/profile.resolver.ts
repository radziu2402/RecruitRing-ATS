import {ProfileService} from "../service/profile.service";
import {inject} from "@angular/core";
import {User} from "../model/user";
import {Observable} from "rxjs";

export const profileResolver: () => Observable<User> = () => {
  return inject(ProfileService).getProfileData();
};
