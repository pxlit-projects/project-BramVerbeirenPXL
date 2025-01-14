// post-detail.component.ts
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { RouterModule } from '@angular/router';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule  } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { PostService } from '../../../services/post.service';
import { Post } from '../../../models/post.model';
import {AuthService} from '../../../guards/auth.service'
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faCheck, faPaperPlane, faTrash } from '@fortawesome/free-solid-svg-icons';
import { CommentSectionComponent } from '../../comments/comment-section.component';


@Component({
  selector: 'app-post-detail',
  templateUrl: './post-detail.component.html',
  styleUrls: ['./post-detail.component.css'],
  imports: [CommonModule, ReactiveFormsModule, RouterModule, CommentSectionComponent, FontAwesomeModule ],
})
export class PostDetailComponent implements OnInit {
  postForm: FormGroup | null = null;
  post: Post | null = null;
  postId: number | null = null;

  faCheck = faCheck;
  faPaperPlane = faPaperPlane;
  faTrash = faTrash;
  
  constructor(
    private route: ActivatedRoute,
    private postService: PostService,
    private fb: FormBuilder,
    private router: Router,
    public authService: AuthService
  ) {}

  ngOnInit() {
    const id = +this.route.snapshot.paramMap.get('id')!;
    this.postId = +id;
    this.postService.getPostById(id).subscribe((post) => {
      this.post = post;
      this.initializeForm(post);
    });
  }

  initializeForm(post: Post) {
    this.postForm = this.fb.group({
      title: [post.title, Validators.required],
      content: [post.content, Validators.required],
      author: [post.author, Validators.required],
      authorEmail: [post.authorEmail, [Validators.required, Validators.email]],
    });
  }

  saveChanges() {
    if (this.postForm && this.post && this.postForm.valid) {
      if (this.postForm.pristine) {
        return; 
      }
      const updatedPost: Post = { ...this.post, ...this.postForm.value };
      this.postService.updatePost(updatedPost).subscribe(() => {
        this.router.navigate(['/posts']);
      });
    } else {
      console.error('Formulier is ongeldig of ontbrekende gegevens.');
    }
  }

  publishPost() {
    if (this.post) {
      const updatedPost: Post = { ...this.post, draft: false };
      this.postService.updatePost(updatedPost).subscribe(() => {
        this.router.navigate(['/posts']);
      });
    }
  }

  deletePost() {
    if (this.post) {
      this.postService.deletePost(this.post.id).subscribe(() => {
        this.router.navigate(['/posts']);
      });
    }
  }
}
