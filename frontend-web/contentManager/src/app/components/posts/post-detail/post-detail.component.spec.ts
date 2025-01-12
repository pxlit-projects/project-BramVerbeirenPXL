
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { PostDetailComponent } from './post-detail.component';
import { PostService } from '../../../services/post.service';
import { CommentService } from '../../../services/comment.service';
import { AuthService } from '../../../guards/auth.service';
import { of } from 'rxjs';
import { ActivatedRoute } from '@angular/router';

class MockAuthService {
  getCurrentUser() {
    return 'Author';
  }

  isEditor() {
    return true; 
  }
}

describe('PostDetailComponent', () => {
  let component: PostDetailComponent;
  let fixture: ComponentFixture<PostDetailComponent>;
  let postServiceSpy: jasmine.SpyObj<PostService>;
  let commentServiceSpy: jasmine.SpyObj<CommentService>;

  beforeEach(async () => {
    postServiceSpy = jasmine.createSpyObj('PostService', ['getPostById', 'deletePost', 'updatePost']);
    commentServiceSpy = jasmine.createSpyObj('CommentService', ['getCommentsByPost']);
  
    postServiceSpy.updatePost.and.returnValue(of({
      id: 1, title: 'Test', content: '', author: '', authorEmail: '', draft: false, approved: true
    }));
    postServiceSpy.getPostById.and.returnValue(of({
      id: 1, title: 'Mock Post', content: 'Mock Content', author: 'Author', authorEmail: 'author@example.com', draft: false, approved: true
    }));
  
    commentServiceSpy.getCommentsByPost.and.returnValue(of([]));
  
    await TestBed.configureTestingModule({
      imports: [RouterTestingModule, PostDetailComponent],
      providers: [
        { provide: PostService, useValue: postServiceSpy },
        { provide: CommentService, useValue: commentServiceSpy },
        { provide: AuthService, useClass: MockAuthService },
        {
          provide: ActivatedRoute,
          useValue: { snapshot: { paramMap: { get: () => '1' } } },
        },
      ],
    }).compileComponents();
  
    fixture = TestBed.createComponent(PostDetailComponent);
    component = fixture.componentInstance;
  });

  it('should load post details on init', () => {
    const post = {
      id: 1,
      title: 'Mock Post',
      content: 'Content',
      author: 'Author',
      authorEmail: 'test@example.com',
      draft: false,
      approved: true,
    };
    postServiceSpy.getPostById.and.returnValue(of(post));

    component.ngOnInit();

    expect(postServiceSpy.getPostById).toHaveBeenCalledWith(1);
    expect(component.post).toEqual(post);
  });

  it('should delete a post', () => {
    postServiceSpy.deletePost.and.returnValue(of(undefined));
    component.post = { id: 1, title: 'Test', content: '', author: '', authorEmail: '', draft: false, approved: true };

    component.deletePost();

    expect(postServiceSpy.deletePost).toHaveBeenCalledWith(1);
  });

  it('should update post', () => {
    const post = {
      id: 1,
      title: 'Mock Post',
      content: 'Mock Content',
      author: 'Author',
      authorEmail: 'test@gmail.com',
      draft: false,
      approved: true,
    };
  
    postServiceSpy.getPostById.and.returnValue(of(post));
    component.ngOnInit();
    fixture.detectChanges();
  
    component.postForm?.setValue({
      title: 'Updated Post',
      content: 'Updated Content',
      author: 'Author',
      authorEmail: 'test@gmail.com',
    });
  
    if (component.postForm) {
      component.postForm.markAsDirty();
    }
    fixture.detectChanges();
  
    expect(component.postForm?.valid).toBeTrue();
  
    component.saveChanges(); 
    expect(postServiceSpy.updatePost).toHaveBeenCalledWith({
      ...post,
      title: 'Updated Post',
      content: 'Updated Content',
    });
  });
  it('should publish a post', () => {
    const post = { id: 1, title: 'Title', content: 'Content', author: 'Author', authorEmail: 'author@example.com', draft: true, approved: false };
    component.post = post;
    postServiceSpy.updatePost.and.returnValue(of({ ...post, draft: false }));
  
    component.publishPost();
    expect(postServiceSpy.updatePost).toHaveBeenCalledWith(jasmine.objectContaining({ draft: false }));
  });
  
  it('should not call updatePost when no changes are made', () => {
    const post = { id: 1, title: 'Same Title', content: 'Same Content', author: 'Author', authorEmail: 'author@example.com', draft: false, approved: true };
    component.post = post;
    component.initializeForm(post);
  
    fixture.detectChanges();
    postServiceSpy.updatePost.calls.reset();
  
    component.saveChanges();
    expect(postServiceSpy.updatePost).not.toHaveBeenCalled();
  });

  afterEach(() => {
    TestBed.resetTestingModule();
  });
});