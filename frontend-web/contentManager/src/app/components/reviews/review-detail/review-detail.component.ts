import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ReviewService } from '../../../services/review.service';
import { Review } from '../../../models/review.model';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-review-detail',
  templateUrl: './review-detail.component.html',
  styleUrls: ['./review-detail.component.css'],
  imports: [CommonModule]
})
export class ReviewDetailComponent implements OnInit {
  review: Review | null = null;

  constructor(
    private route: ActivatedRoute,
    private reviewService: ReviewService
  ) {}

  ngOnInit(): void {
    const postId = Number(this.route.snapshot.paramMap.get('postId'));
    this.getReviewDetail(postId);
  }

  getReviewDetail(postId: number): void {
    this.reviewService.getPendingReviews().subscribe({
      next: (reviews) => {
        this.review = reviews.find((r: { postId: number; }) => r.postId === postId) || null;
      },
      error: (err) => console.error('Fout bij ophalen review:', err),
    });
  }
}
