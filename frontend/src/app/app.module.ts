import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { RouterModule } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatCardModule } from '@angular/material/card';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';

import { AppComponent } from './app.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { FooterComponent } from './components/footer/footer.component';
import { SingnupComponent } from './pages/singnup/singnup.component';
import { LoginComponent } from './pages/login/login.component';
import { UsersComponent } from './pages/users/users.component';
import { AdminGuard } from './services/admin.guard';
import { LoginService } from './services/login.service';
import { UserGuard } from './services/user.guard';
import { routes } from './app.routes';
import { UserService } from './services/user.service';
import { UserDashboardComponent } from './pages/user/user-dashboard/user-dashboard.component';
import { HomeComponent } from './pages/home/home.component';
import { DashboardComponent } from './pages/admin/dashboard/dashboard.component';
import { NotFoundComponent } from './components/not-found/not-found.component';

@NgModule({
    declarations: [
        AppComponent,
        NavbarComponent,
        FooterComponent,
        SingnupComponent,
        LoginComponent,
        UsersComponent,
        UserDashboardComponent,
        HomeComponent,
        DashboardComponent,
        NotFoundComponent,
        NavbarComponent
        // Other components, guards, and services...
    ],
    imports: [
        BrowserModule,
        BrowserAnimationsModule,
        HttpClientModule,
        FormsModule,
        ReactiveFormsModule,
        MatButtonModule,
        MatInputModule,
        MatFormFieldModule,
        MatCardModule,
        MatToolbarModule,
        MatIconModule,
        HttpClient,

        // Other necessary modules...
    ],
    providers: [
        AdminGuard,
        LoginService,
        UserGuard,
        UserService,
        // Other services...
    ],
    bootstrap: [AppComponent],
})
export class AppModule { }
