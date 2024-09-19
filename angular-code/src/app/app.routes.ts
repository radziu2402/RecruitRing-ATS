import {Routes} from '@angular/router';
import {HomeComponent} from './home/home.component';
import {LoginComponent} from './auth/login/login.component';
import {JobListComponent} from './job-postings/job-list/job-list.component';
import {DashboardComponent} from './dashboard/dashboard.component';
import {JobManagementComponent} from './dashboard/job-management/job-management.component';
import {RecruitmentManagementComponent} from './dashboard/recruitment-management/recruitment-management.component';
import {CandidateManagementComponent} from './dashboard/candidate-management/candidate-management.component';
import {HomeGuard} from './core/service/guard/home.guard';
import {LoginGuard} from './core/service/guard/login.guard';
import {AuthGuard} from './core/service/guard/auth.guard';

export const routes: Routes = [
  {path: '', component: HomeComponent, canActivate: [HomeGuard]},
  {path: 'login', component: LoginComponent, canActivate: [LoginGuard]},
  {path: 'job-listings', component: JobListComponent},
  {
    path: 'dashboard',
    component: DashboardComponent,
    canActivate: [AuthGuard],
    children: [
      {path: 'jobs', component: JobManagementComponent},
      {path: 'recruitment', component: RecruitmentManagementComponent},
      {path: 'candidates', component: CandidateManagementComponent}
    ]
  },
  {path: '**', redirectTo: ''}
];
