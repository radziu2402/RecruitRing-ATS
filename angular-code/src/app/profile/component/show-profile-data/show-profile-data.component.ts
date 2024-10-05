import {Component, Input} from '@angular/core';
import {User} from "../../model/user";
import {CommonModule, NgOptimizedImage} from '@angular/common';

@Component({
  selector: 'show-profile-data',
  standalone: true,
  imports: [CommonModule, NgOptimizedImage],
  templateUrl: './show-profile-data.component.html',
  styleUrls: ['./show-profile-data.component.scss']
})
export class ShowProfileDataComponent {
  @Input()
  profileData!: User;
}
