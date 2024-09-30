import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, RouterModule} from "@angular/router";
import {User} from "../../model/user";
import {CommonModule, NgOptimizedImage} from '@angular/common';
import {MatIcon} from "@angular/material/icon";
import {ShowProfileDataComponent} from "../show-profile-data/show-profile-data.component";
import {ChangeProfileDataComponent} from "../change-profile-data/change-profile-data.component";

@Component({
  selector: 'app-user-profile',
  standalone: true,
  imports: [CommonModule, RouterModule, MatIcon, NgOptimizedImage, ShowProfileDataComponent, ChangeProfileDataComponent],
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.scss']
})
export class UserProfileComponent implements OnInit {
  profileData!: User;
  mode: profileMode = 'show';

  constructor(private route: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.profileData = this.route.snapshot.data['userData'];
  }

  navigate() {
    this.mode = 'change';
  }

  isShowMode() {
    return this.mode === 'show';
  }

  isChangeMode() {
    return this.mode === 'change';
  }

  updateData(data: User) {
    this.profileData = data;
    this.mode = 'show';
  }
}

type profileMode = 'change' | 'show';
