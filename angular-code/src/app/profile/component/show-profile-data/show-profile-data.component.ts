import {Component, Input} from '@angular/core';
import {User} from "../../model/user";
import {CommonModule} from '@angular/common';

@Component({
  selector: 'show-profile-data',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './show-profile-data.component.html',
  styleUrls: ['./show-profile-data.component.scss']
})
export class ShowProfileDataComponent {
  @Input()
  profileData!: User;
}
