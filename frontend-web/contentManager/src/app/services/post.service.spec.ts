import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { PostService } from './post.service';
import { Post } from '../models/post.model';

describe('PostService', () => {
  let service: PostService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [PostService],
    });
    service = TestBed.inject(PostService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify(); // Zorgt ervoor dat er geen openstaande verzoeken zijn.
  });

  it('should retrieve all posts', () => {
    const mockPosts: Post[] = [
      { id: 1, title: 'Post 1', content: 'Content 1', author: 'Author 1', authorEmail: 'author1@test.com', draft: false, approved: true },
      { id: 2, title: 'Post 2', content: 'Content 2', author: 'Author 2', authorEmail: 'author2@test.com', draft: true, approved: false },
    ];

    service.getAllPosts().subscribe((posts) => {
      expect(posts.length).toBe(2);
      expect(posts).toEqual(mockPosts);
    });

    const req = httpMock.expectOne('http://localhost:8081/api/posts');
    expect(req.request.method).toBe('GET');
    req.flush(mockPosts);
  });

  it('should retrieve a post by ID', () => {
    const mockPost: Post = { id: 1, title: 'Post 1', content: 'Content 1', author: 'Author 1', authorEmail: 'author1@test.com', draft: false, approved: true };

    service.getPostById(1).subscribe((post) => {
      expect(post).toEqual(mockPost);
    });

    const req = httpMock.expectOne('http://localhost:8081/api/posts/1');
    expect(req.request.method).toBe('GET');
    req.flush(mockPost);
  });

  it('should create a new post', () => {
    const newPost: Post = { id: 3, title: 'New Post', content: 'New Content', author: 'Author 3', authorEmail: 'author3@test.com', draft: true, approved: false };

    service.createPost(newPost).subscribe((post) => {
      expect(post).toEqual(newPost);
    });

    const req = httpMock.expectOne('http://localhost:8081/api/posts');
    expect(req.request.method).toBe('POST');
    req.flush(newPost);
  });

  it('should update a post', () => {
    const updatedPost: Post = { id: 1, title: 'Updated Post', content: 'Updated Content', author: 'Author 1', authorEmail: 'author1@test.com', draft: false, approved: true };

    service.updatePost(updatedPost).subscribe((post) => {
      expect(post).toEqual(updatedPost);
    });

    const req = httpMock.expectOne('http://localhost:8081/api/posts/1');
    expect(req.request.method).toBe('PUT');
    req.flush(updatedPost);
  });

  it('should delete a post', () => {
    service.deletePost(1).subscribe((response) => {
      expect(response).toEqual(Object({  })); // Controleer op een leeg object
    });
  
    const req = httpMock.expectOne('http://localhost:8081/api/posts/1');
    expect(req.request.method).toBe('DELETE');
    req.flush({}); // Stuur een leeg object als respons
  });
});
