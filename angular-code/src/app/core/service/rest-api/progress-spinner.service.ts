import {Injectable} from '@angular/core';
import {Subject} from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class ProgressSpinnerService {
  private showSpinner$ = new Subject<boolean>();

  getSpinnerStream$ = this.showSpinner$.asObservable();

  showSpinner(value: boolean): void {
    this.showSpinner$.next(value);
  }
}
