import { Component, OnInit } from '@angular/core';
import { ReviewService } from '../../../services/review.service';
import { Review } from '../../../models/review.model';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../../guards/auth.service';
import { MatDialogModule } from '@angular/material/dialog';  // Gebruik MatDialogModule in plaats van MatDialog
import { RejectionDialogComponent } from './rejection-dialog.component';
import { MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'app-review-list',
  templateUrl: './review-list.component.html',
  styleUrls: ['./review-list.component.css'],
  standalone: true, // Zorg ervoor dat standalone wordt ingesteld
  imports: [CommonModule, FormsModule, MatDialogModule],  // Gebruik MatDialogModule hier
})
export class ReviewListComponent implements OnInit {
  reviews: Review[] = [];
  oldReviews: Review[] = [];
  showRejectModal: boolean = false;
  rejectionComment: string = '';
  currentPostId: number | null = null;

  constructor(
    private authService: AuthService,
    private reviewService: ReviewService,
    private dialog: MatDialog  // Injecteer hier de MatDialog service
  ) {}

  ngOnInit() {
    this.loadReviews();
  }

  loadReviews() {
    const currentUser = this.authService.getCurrentUser();
  
    this.reviewService.getPendingReviews().subscribe((reviews) => {
      this.reviews = reviews.filter(
        (review: { author: string | null; rejectionComment: string | null }) =>
          review.author !== currentUser && review.rejectionComment === null
      );
      this.oldReviews = reviews.filter(
        (review: { author: string | null; rejectionComment: string | null }) =>
          review.author !== currentUser
      );
    });
  }

  approveReview(postId: number) {
    this.reviewService.approvePost(postId, { rejectionComment: "", approved: true }).subscribe(() => {
      alert('Review goedgekeurd!');
      this.loadReviews();
    });
  }

  openRejectModal(postId: number) {
    const dialogRef = this.dialog.open(RejectionDialogComponent, {
      width: '300px',
      data: { postId },
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.reviewService.rejectPost(postId, { rejectionComment: result, approved: false }).subscribe(() => {
          alert('Review afgewezen!');
          this.loadReviews();
        });
      }
    });
  }

  closeRejectModal() {
    this.showRejectModal = false;
    this.rejectionComment = '';
  }

  submitRejection() {
    if (this.currentPostId && this.rejectionComment.trim()) {
      this.reviewService.rejectPost(this.currentPostId, { rejectionComment: this.rejectionComment, approved: false }).subscribe(() => {
        alert('Review afgewezen!');
        this.loadReviews();
        this.closeRejectModal();
      });
    } else {
      alert('Vul een reden voor afwijzing in.');
    }
  }
}
