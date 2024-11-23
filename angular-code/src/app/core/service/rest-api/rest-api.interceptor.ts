import {HttpInterceptorFn} from '@angular/common/http';
import {finalize} from 'rxjs';
import {inject} from '@angular/core';
import {ProgressSpinnerService} from "./progress-spinner.service";

export const restApiInterceptor: HttpInterceptorFn = (req, next) => {
  const spinnerService = inject(ProgressSpinnerService);

  const skipSpinner = req.headers.has('X-Skip-Spinner');

  if (!skipSpinner) {
    spinnerService.showSpinner(true);
  }

  return next(req).pipe(
    finalize(() => {
      if (!skipSpinner) {
        spinnerService.showSpinner(false);
      }
    })
  );
};
