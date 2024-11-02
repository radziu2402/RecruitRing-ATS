import { inject } from '@angular/core';
import { ResolveFn } from '@angular/router';
import { RecruitmentService } from '../service/recruitment.service';
import { Observable } from 'rxjs';

export const recruitmentListResolver: ResolveFn<Observable<any[]>> = () => {
  const recruitmentService = inject(RecruitmentService);
  return recruitmentService.getRecruitments();
};
