import {Routes} from '@angular/router';
import {HomeComponent} from './home/home.component';
import {LoginComponent} from './auth/login/login.component';
import {DashboardComponent} from './dashboard/dashboard.component';
import {JobManagementComponent} from './dashboard/job-management/job-management.component';
import {
  RecruitmentManagementComponent
} from './dashboard/recruitment/recruitment-management/recruitment-management.component';
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
import {profileResolver} from "./profile/resolver/profile.resolver";
import {UserProfileComponent} from "./profile/component/user-profile/user-profile.component";
import {ResetPasswordComponent} from "./auth/reset-password/reset-password.component";
import {SetNewPasswordComponent} from "./auth/set-new-password/set-new-password.component";
import {ErrorPageComponent} from "./auth/error-page/error-page.component";
import {PublicJobListComponent} from "./job-postings/public-job-list/public-job-list.component";
import {PublicJobListResolver} from "./job-postings/resolver/public-job-list-resolver.service";
import {PublicJobDetailComponent} from "./job-postings/public-job-detail/public-job-detail.component";
import {
  PublicJobApplicationFormComponent
} from "./job-postings/public-job-application-form/public-job-application-form.component";
import {
  ApplicationSuccessConfirmationComponent
} from "./job-postings/application-success-confirmation/application-success-confirmation.component";
import {RecruitmentListComponent} from "./dashboard/recruitment/recruitment-list/recruitment-list.component";
import {CalendarComponent} from "./dashboard/calendar/calendar.component";
import {recruitmentListResolver} from "./dashboard/recruitment/resolver/recruitment-list.resolver";
import {CandidateDetailsComponent} from "./dashboard/recruitment/candidate-details/candidate-details.component";
import {candidateDetailsResolver} from "./dashboard/recruitment/resolver/candidate-details.resolver";

export const routes: Routes = [
  {path: '', component: HomeComponent, canActivate: [HomeGuard]},
  {path: 'login', component: LoginComponent, canActivate: [LoginGuard]},
  {path: 'reset-password', component: ResetPasswordComponent},
  {path: 'set-new-password', component: SetNewPasswordComponent},
  {path: 'error-page', component: ErrorPageComponent},
  {
    path: 'jobs',
    children: [
      {
        path: '',
        component: PublicJobListComponent,
        resolve: {jobs: PublicJobListResolver},
      },
      {
        path: ':offerCode',
        component: PublicJobDetailComponent
      },
      {
        path: 'apply/:offerCode',
        component: PublicJobApplicationFormComponent
      },
      {
        path: 'apply/:offerCode/confirmation',
        component: ApplicationSuccessConfirmationComponent
      }
    ]
  },
  {
    path: 'dashboard',
    component: DashboardComponent,
    canActivate: [AuthGuard],
    children: [
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
        component: RecruitmentListComponent,
        resolve: {recruitments: recruitmentListResolver}
      },
      {
        path: 'recruitment/:offerCode',
        component: RecruitmentManagementComponent
      },
      {
        path: 'recruitment/:offerCode/candidate/:applicationCode',
        component: CandidateDetailsComponent,
        resolve: {candidate: candidateDetailsResolver}
      },
      {
        path: 'candidates',
        component: CandidateManagementComponent
      },
      {
        path: 'calendar',
        component: CalendarComponent
      },
      {
        path: 'admin',
        component: AdminComponent,
        canActivate: [AdminGuard],
      },
      {
        path: 'profile',
        component: UserProfileComponent,
        resolve: {userData: profileResolver}
      },
      {
        path: '',
        pathMatch: 'full',
        redirectTo: 'home'
      }
    ]
  },
  {path: '**', redirectTo: ''},

];
