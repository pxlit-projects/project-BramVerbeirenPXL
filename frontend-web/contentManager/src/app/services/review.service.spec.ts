import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { ReviewService } from './review.service';

describe('ReviewService', () => {
  let service: ReviewService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [ReviewService],
    });
    service = TestBed.inject(ReviewService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should retrieve pending reviews', () => {
    const mockPendingReviews = [
      { id: 1, postId: 1, reviewer: 'Reviewer 1', status: 'pending' },
      { id: 2, postId: 2, reviewer: 'Reviewer 2', status: 'pending' },
    ];

    service.getPendingReviews().subscribe((reviews) => {
      expect(reviews.length).toBe(2);
      expect(reviews).toEqual(mockPendingReviews);
    });

    const req = httpMock.expectOne('http://localhost:8082/api/reviews/pending');
    expect(req.request.method).toBe('GET');
    req.flush(mockPendingReviews);
  });

  it('should approve a post', () => {
    const data = { rejectionComment: '', approved: true };
  
    service.approvePost(1, data).subscribe((response) => {
      expect(response).toEqual(Object({  })); // Controleer op een leeg object
    });
  
    const req = httpMock.expectOne('http://localhost:8082/api/reviews/1/approve');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(data);
    req.flush({}); // Stuur een leeg object als respons
  });
  
  it('should reject a post', () => {
    const data = { rejectionComment: 'Not appropriate content', approved: false };
  
    service.rejectPost(1, data).subscribe((response) => {
      expect(response).toEqual(Object({  })); // Controleer op een leeg object
    });
  
    const req = httpMock.expectOne('http://localhost:8082/api/reviews/1/reject');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(data);
    req.flush({}); // Stuur een leeg object als respons
  });
});
