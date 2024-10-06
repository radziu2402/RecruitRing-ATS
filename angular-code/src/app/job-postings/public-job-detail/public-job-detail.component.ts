import {Component, Input} from '@angular/core';

@Component({
  selector: 'app-public-job-detail',
  standalone: true,
  imports: [],
  templateUrl: './public-job-detail.component.html',
  styleUrl: './public-job-detail.component.scss'
})
export class PublicJobDetailComponent {
  @Input() job: any;

  // Metoda do obsługi aplikacji
  applyForJob() {
    // Logika otwarcia formularza aplikacji
  }
}
