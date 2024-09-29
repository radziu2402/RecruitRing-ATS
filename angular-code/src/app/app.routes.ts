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
import {AdminGuard} from "./core/service/guard/admin.guard";
import {AdminComponent} from "./admin/admin.component";
import {JobManagementResolver} from "./dashboard/job-management/resolver/job-management.resolver";
import {JobDetailComponent} from "./dashboard/job-management/job-detail/job-detail.component";
import {CreateUpdateJobComponent} from "./dashboard/job-management/create-update-job/create-update-job.component";
import {MainPanelComponent} from "./dashboard/main-panel/main-panel.component";

export const routes: Routes = [
  {path: '', component: HomeComponent, canActivate: [HomeGuard]},
  {path: 'login', component: LoginComponent, canActivate: [LoginGuard]},
  {path: 'job-listings', component: JobListComponent},
  {
    path: 'dashboard',
    component: DashboardComponent,
    canActivate: [AuthGuard],
    children: [
      {
        path: '',
        pathMatch: 'full',
        redirectTo: 'home'
      },
      {
        path: 'home',
        component: MainPanelComponent
      },
      {
        path: 'jobs',
        component: JobManagementComponent,
        resolve: {jobs: JobManagementResolver}
      },
      {
        path: 'jobs/:offerCode',
        component: JobDetailComponent
      },
      {
        path: 'create-job',
        component: CreateUpdateJobComponent
      },
      {
        path: 'edit-job/:offerCode',
        component: CreateUpdateJobComponent
      },
      {
        path: 'recruitment',
        component: RecruitmentManagementComponent
      },
      {
        path: 'candidates',
        component: CandidateManagementComponent
      },
      {
        path: 'admin',
        component: AdminComponent,
        canActivate: [AdminGuard],
      }
    ]
  },
  {path: '**', redirectTo: ''}
];
