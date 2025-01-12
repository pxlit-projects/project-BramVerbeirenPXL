import { Component, Input, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faEdit, faTrashAlt } from '@fortawesome/free-solid-svg-icons';
import { ReactiveFormsModule  } from '@angular/forms';
import { CommentService } from '../../services/comment.service';
import { Comment } from '../../models/comment.model';
import { FormsModule } from '@angular/forms';
import {AuthService} from '../../guards/auth.service'


@Component({
  selector: 'app-comment-section',
  templateUrl: './comment-section.component.html',
  styleUrls: ['./comment-section.component.css'],
  imports: [CommonModule, ReactiveFormsModule, RouterModule, FormsModule , FontAwesomeModule],
})
export class CommentSectionComponent implements OnInit {
  @Input() postId!: number;
  comments: Comment[] = [];
  newCommentContent: string = '';
  editingComment: Comment | null = null;
  editedContent: string = '';

  faEdit = faEdit;
  faTrashAlt = faTrashAlt;

  constructor(private commentService: CommentService, private authService: AuthService) {}

  ngOnInit() {
    this.loadComments();
  }

  loadComments() {
    this.commentService.getCommentsByPost(this.postId).subscribe((comments) => {
      this.comments = comments;
    });
  }

  addComment() {
    if (this.newCommentContent.trim()) {
      const newComment = {
        postId: this.postId,
        author: this.authService.getCurrentUser() ?? 'Onbekend',
        content: this.newCommentContent,
      };
      this.commentService.addComment(this.postId, newComment).subscribe(() => {
        this.newCommentContent = '';
        this.loadComments();
      });
    }
  }

  startEditComment(comment: Comment) {
    this.editingComment = comment;
    this.editedContent = comment.content; // Zet de originele tekst in het invoerveld
  }

  cancelEdit() {
    this.editingComment = null;
    this.editedContent = '';
  }

  saveEdit(comment: Comment) {
    if (this.editedContent.trim() && this.editedContent !== comment.content) {
      this.commentService.updateComment(comment.id, this.editedContent).subscribe(() => {
        this.loadComments();
        this.editingComment = null;
        this.editedContent = '';
      });
    }
  }

  deleteComment(commentId: number) {
    if (confirm('Weet je zeker dat je deze reactie wilt verwijderen?')) {
      this.commentService.deleteComment(commentId).subscribe(() => {
        this.loadComments();
      });
    }
  }

  isCurrentUser(author: string): boolean {
    return author === this.authService.getCurrentUser();
  }
}