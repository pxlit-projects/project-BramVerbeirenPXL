import { TestBed } from '@angular/core/testing';
import { CommentService } from './comment.service';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

describe('CommentService', () => {
  let service: CommentService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [CommentService], // Voeg hier de service toe
      teardown: { destroyAfterEach: false }
    });
    service = TestBed.inject(CommentService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  it('should fetch comments by post', () => {
    const mockComments = [
      {
        id: 1,
        postId: 1,
        author: 'Test Author',
        content: 'Comment 1',
        createdDate: new Date(),
      },
    ];

    service.getCommentsByPost(1).subscribe((comments) => {
      expect(comments).toEqual(mockComments);
    });

    const req = httpMock.expectOne('http://localhost:8083/api/comments/1');
    expect(req.request.method).toBe('GET');
    req.flush(mockComments);
  });

  afterEach(() => {
    httpMock.verify(); // Zorgt ervoor dat er geen onbeantwoorde requests zijn
  });
});
