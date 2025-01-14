import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReviewDetailComponent } from './review-detail.component';
import { ReviewService } from '../../../services/review.service';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

describe('ReviewDetailComponent', () => {
  let component: ReviewDetailComponent;
  let fixture: ComponentFixture<ReviewDetailComponent>;
  let mockReviewService: jasmine.SpyObj<ReviewService>;

  beforeEach(async () => {
    mockReviewService = jasmine.createSpyObj('ReviewService', ['getPendingReviews']);

    await TestBed.configureTestingModule({
      imports: [ReviewDetailComponent], 
      providers: [
        { provide: ReviewService, useValue: mockReviewService },
        { provide: ActivatedRoute, useValue: { snapshot: { paramMap: { get: () => '1' } } } },
      ],
      teardown: {destroyAfterEach: false}
    }).compileComponents();

    fixture = TestBed.createComponent(ReviewDetailComponent);
    component = fixture.componentInstance;
  });

  it('should load review detail', () => {
    const review = {
      id: 1,
      postId: 1,
      title: 'Test Review',
      content: 'Review content',
      author: 'Reviewer',
      approved: false,
    };
    mockReviewService.getPendingReviews.and.returnValue(of([review]));
    component.ngOnInit();
    expect(component.review).toEqual(review);
  });
});
