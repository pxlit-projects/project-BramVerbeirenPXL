//post-form.component.ts
import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router  } from '@angular/router';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { PostService } from '../../../services/post.service';
import { AuthService } from '../../../guards/auth.service';
import { RouterModule } from '@angular/router';


@Component({
  selector: 'app-post-form',
  templateUrl: './post-form.component.html',
  styleUrls: ['./post-form.component.css'],
  imports: [CommonModule, ReactiveFormsModule, RouterModule]
})
export class PostFormComponent {
  postForm: FormGroup;

  constructor(private fb: FormBuilder, private postService: PostService, private router: Router, public authService: AuthService) {
    // Initialiseer het formulier
    this.postForm = this.fb.group({
      title: ['', Validators.required],
      content: ['', Validators.required],
      author: ['', Validators.required],
      authorEmail: ['', [Validators.required, Validators.email]],
    });
  }

  submitPost(isDraft: boolean) {
    if (this.postForm.valid) {
      const post = { ...this.postForm.value, draft: isDraft };
      this.postService.createPost(post).subscribe(() => {
        this.router.navigate(['/posts']);
      }, (error) => {
        console.error('Fout bij het opslaan:', error);
        alert('Er is een fout opgetreden bij het opslaan van de post.');
      });
    } else {
      alert('Vul alle verplichte velden in.');
    }
  }

  onSubmit() {
    this.submitPost(false);
  }

  saveAsDraft() {
    this.submitPost(true);
  }
}