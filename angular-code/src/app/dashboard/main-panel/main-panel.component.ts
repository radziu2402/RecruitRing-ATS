import { Component } from '@angular/core';
import {Router} from "@angular/router";

@Component({
  selector: 'app-main-panel',
  standalone: true,
  imports: [],
  templateUrl: './main-panel.component.html',
  styleUrl: './main-panel.component.scss'
})
export class MainPanelComponent {

  constructor(private router: Router) {}

  navigateTo(section: string) {
    this.router.navigate([`/dashboard/${section}`]);
  }
}
