import {AfterContentChecked, ChangeDetectionStrategy, ChangeDetectorRef, Component, HostListener} from '@angular/core';
import {MatProgressSpinner} from "@angular/material/progress-spinner";
import {NgIf} from "@angular/common";
import {ProgressSpinnerService} from "../service/rest-api/progress-spinner.service";

@Component({
  selector: 'app-progress-spinner',
  standalone: true,
  imports: [
    MatProgressSpinner,
    NgIf
  ],
  templateUrl: './progress-spinner.component.html',
  styleUrl: './progress-spinner.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ProgressSpinnerComponent implements AfterContentChecked {
  shouldShowSpinner: boolean = false;

  constructor(
    private readonly progressSpinnerService: ProgressSpinnerService,
    private readonly cdr: ChangeDetectorRef
  ) {}

  @HostListener('window:keydown.tab', ['$event'])
  @HostListener('window:keydown.shift.tab', ['$event'])
  handleFocusChange(event: KeyboardEvent): void {
    if (this.shouldShowSpinner) {
      event.preventDefault();
    }
  }

  ngAfterContentChecked(): void {
    this.progressSpinnerService.getSpinnerStream$.subscribe((shouldShow) => {
      this.shouldShowSpinner = shouldShow;
      this.cdr.detectChanges();
    });
  }
}
