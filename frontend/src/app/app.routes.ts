import { NgModule } from '@angular/core';
import { RouterModule, Routes, mapToCanActivate } from '@angular/router';
import { SingnupComponent } from './pages/singnup/singnup.component';
import { LoginComponent } from './pages/login/login.component';
import { UsersComponent } from './pages/users/users.component';
import { HomeComponent } from './pages/home/home.component';
import { AuthInterceptor } from './services/auth.interceptor';
import { HTTP_INTERCEPTORS, HttpClient, HttpClientModule } from '@angular/common/http';
import { DashboardComponent } from './pages/admin/dashboard/dashboard.component';
import { UserDashboardComponent } from './pages/user/user-dashboard/user-dashboard.component';
import { AdminGuard } from './services/admin.guard';
import { LoginService } from './services/login.service';
import { UserGuard } from './services/user.guard';
import { NotFoundComponent } from './components/not-found/not-found.component';
import { ProfileComponent } from './pages/profile/profile.component';
import { WelcomeComponent } from './pages/admin/welcome/welcome.component';
import { SidebarComponent } from './pages/admin/sidebar/sidebar.component';


export const routes: Routes = [
  {
    path: 'signup',
    component: SingnupComponent,
    pathMatch: 'full'
  },
  {
    path: 'login',
    component: LoginComponent,
    pathMatch: 'full',
  },
  {
    path: 'home',
    component: HomeComponent,
    pathMatch: 'full',
  },
  {
    path: 'sidebar',
    component: SidebarComponent,
    canActivate: [AdminGuard],
    children: [
      {
        path: 'profile',
        component: ProfileComponent,
        canActivate: [AdminGuard],
      }
    ]
  },
  {
    path: 'profile',
    component: ProfileComponent,
    pathMatch: 'full',
    canActivate: [AdminGuard],
  },
  {
    path: 'user/all',
    component: UsersComponent,
    pathMatch: 'full',
    canActivate: [AdminGuard],
  },
  {
    path: 'admins',
    component: DashboardComponent,
    pathMatch: 'full',
    canActivate: [AdminGuard],
    // children:[
    //   {
    //     path:'',
    //     component:WelcomeComponent,
    //     canActivate:[AdminGuard],
    //   },
    //   {
    //     path:'profile',
    //     component:ProfileComponent,
    //     canActivate:[AdminGuard],
    //   }
    // ]
  },
  {
    path: 'user-dashboard',
    component: UserDashboardComponent,
    pathMatch: 'full',
    canActivate: [UserGuard],
    
  }, {
    path: '**', component: NotFoundComponent
  },


];

@NgModule({
  imports: [HttpClientModule, RouterModule.forRoot(routes)],
  exports: [RouterModule],
  providers: []
})
export class AppRoutingModule { }
