
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReviewActionsComponent } from './review-actions.component';
import { ReviewService } from '../../../services/review.service';
import { of } from 'rxjs';

describe('ReviewActionsComponent', () => {
  let component: ReviewActionsComponent;
  let fixture: ComponentFixture<ReviewActionsComponent>;
  let reviewServiceSpy: jasmine.SpyObj<ReviewService>;

  beforeEach(async () => {
    reviewServiceSpy = jasmine.createSpyObj('ReviewService', ['approvePost', 'rejectPost']);

    await TestBed.configureTestingModule({
      imports: [ReviewActionsComponent],
      providers: [{ provide: ReviewService, useValue: reviewServiceSpy }],
    }).compileComponents();

    fixture = TestBed.createComponent(ReviewActionsComponent);
    component = fixture.componentInstance;
    component.postId = 1;
  });

  it('should approve a post', () => {
    reviewServiceSpy.approvePost.and.returnValue(of());
    component.approve();
    expect(reviewServiceSpy.approvePost).toHaveBeenCalledWith(1, { rejectionComment: '', approved: true });
  });

  it('should reject a post', () => {
    component.rejectionComment = 'Reason';
    reviewServiceSpy.rejectPost.and.returnValue(of());
    component.reject();
    expect(reviewServiceSpy.rejectPost).toHaveBeenCalledWith(1, { rejectionComment: 'Reason', approved: false });
  });

  afterEach(() => {
    TestBed.resetTestingModule();
  });
});
