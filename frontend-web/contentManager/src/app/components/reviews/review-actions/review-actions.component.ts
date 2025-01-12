//review-actions.component.ts
import { Component, Input } from '@angular/core';
import { ReviewService } from '../../../services/review.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-review-actions',
  templateUrl: './review-actions.component.html',
  styleUrls: ['./review-actions.component.css'],
  imports: [CommonModule]
})
export class ReviewActionsComponent {
  @Input() postId!: number;
  rejectionComment: string = '';

  constructor(private reviewService: ReviewService) {}

  approve(): void {
    this.reviewService.approvePost(this.postId, { rejectionComment: "", approved: true }).subscribe(() => {
      alert('Review goedgekeurd!');
      window.location.reload();
    });
  }

  reject(): void {
    if (this.rejectionComment.trim()) {
      this.reviewService.rejectPost(this.postId, { rejectionComment: this.rejectionComment, approved: false }).subscribe(() => {
        alert('Review afgewezen!');
        window.location.reload();
      });
    } else {
      alert('Vul een opmerking in bij de afwijzing.');
    }
  }
}
