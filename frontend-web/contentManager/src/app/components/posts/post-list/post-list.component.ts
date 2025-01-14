import { Component, OnInit } from '@angular/core';
import { PostService } from '../../../services/post.service';
import { Post } from '../../../models/post.model';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { AuthService } from '../../../guards/auth.service';
import { FormsModule } from '@angular/forms';
import { MatDatepickerModule } from '@angular/material/datepicker';



@Component({
  selector: 'app-post-list',
  templateUrl: './post-list.component.html',
  styleUrls: ['./post-list.component.css'],
  imports: [CommonModule, RouterModule, FormsModule, MatDatepickerModule]
})
export class PostListComponent implements OnInit {
  publishedPosts: Post[] = [];
  draftPosts: Post[] = [];
  filteredPublishedPosts: Post[] = [];
  filteredDraftPosts: Post[] = [];
  filterText: string = '';
  filterDate: string | null = null;

  constructor(public authService: AuthService, public postService: PostService) {}

  ngOnInit() {
    this.loadPosts();
  }

  loadPosts() {
    const currentUser = this.authService.getCurrentUser();
    const isEditor = this.authService.isEditor();

    this.postService.getAllPosts().subscribe((posts: Post[]) => {
      this.publishedPosts = posts.filter(post => !post.draft && post.approved);
      this.filteredPublishedPosts = [...this.publishedPosts]; // initialisatie
      
      if (isEditor) {
        this.draftPosts = posts.filter(post => post.author === currentUser);
        this.filteredDraftPosts = [...this.draftPosts];
      }
    });
  }

  filterPosts() {
    this.filteredPublishedPosts = this.publishedPosts.filter(post =>
      (this.filterText ? (post.title.toLowerCase().includes(this.filterText.toLowerCase()) || post.author.toLowerCase().includes(this.filterText.toLowerCase())) : true) &&
      (this.filterDate ? (post.createdDate ? new Date(post.createdDate).toISOString().slice(0, 10) === this.filterDate : false) : true)
    );
  
    this.filteredDraftPosts = this.draftPosts.filter(post =>
      (this.filterText ? (post.title.toLowerCase().includes(this.filterText.toLowerCase()) || post.author.toLowerCase().includes(this.filterText.toLowerCase())) : true) &&
      (this.filterDate ? (post.createdDate ? new Date(post.createdDate).toISOString().slice(0, 10) === this.filterDate : false) : true)
    );
  }
  
}
