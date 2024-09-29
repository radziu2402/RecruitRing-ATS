import { HttpInterceptorFn } from '@angular/common/http';
import { finalize } from 'rxjs';
import { inject } from '@angular/core';
import { shouldShowSpinnerOnRequest } from './rest-api-exceptions';
import {ProgressSpinnerService} from "./progress-spinner.service";

export const restApiInterceptor: HttpInterceptorFn = (req, next) => {
  const spinnerService = inject(ProgressSpinnerService);
  const showSpinner = shouldShowSpinnerOnRequest(req);

  if (showSpinner) {
    spinnerService.showSpinner(true);
  }

  return next(req).pipe(
    finalize(() => {
      if (showSpinner) {
        spinnerService.showSpinner(false);
      }
    })
  );
};
