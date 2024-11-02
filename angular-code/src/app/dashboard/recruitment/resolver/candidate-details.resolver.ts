import {inject} from '@angular/core';
import {ResolveFn} from '@angular/router';
import {CandidateService} from '../service/candidate.service';
import {Observable, of} from 'rxjs';
import {catchError} from 'rxjs/operators';
import {DetailedCandidateDTO} from "../model/detailed-candidate.model";

export const candidateDetailsResolver: ResolveFn<Observable<DetailedCandidateDTO | null>> = (route, state) => {
  const candidateService = inject(CandidateService);
  const applicationCode = route.paramMap.get('applicationCode');

  if (applicationCode) {
    return candidateService.getCandidateDetails(applicationCode).pipe(
      catchError(() => of(null))
    );
  }
  return of(null);
};
