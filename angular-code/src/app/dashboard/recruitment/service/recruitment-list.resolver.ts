import { Injectable } from '@angular/core';
import { Resolve } from '@angular/router';
import { Observable } from 'rxjs';
import { RecruitmentService } from './recruitment.service';

@Injectable({
  providedIn: 'root'
})
export class RecruitmentListResolver implements Resolve<any[]> {

  constructor(private recruitmentService: RecruitmentService) {}

  resolve(): Observable<any[]> {
    return this.recruitmentService.getRecruitments();
  }
}
