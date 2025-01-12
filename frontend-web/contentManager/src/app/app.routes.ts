//app.routes.ts
import { Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { PostListComponent } from './components/posts/post-list/post-list.component';
import { PostDetailComponent } from './components/posts/post-detail/post-detail.component';
import { PostFormComponent } from './components/posts/post-form/post-form.component';
import { ReviewListComponent } from './components/reviews/review-list/review-list.component';
import { LoginComponent } from './components/login/login.component';


export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'posts', component: PostListComponent },
  { path: 'posts/new', component: PostFormComponent },
  { path: 'posts/:id', component: PostDetailComponent },
  { path: 'reviews', component: ReviewListComponent },
  { path: 'login', component: LoginComponent },
  { path: '**', redirectTo: '', pathMatch: 'full' } // Fallback route
];
