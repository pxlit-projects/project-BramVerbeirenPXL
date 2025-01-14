
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { PostListComponent } from './post-list.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { PostService } from '../../../services/post.service';
import { AuthService } from '../../../guards/auth.service';
import { of } from 'rxjs';
import { Component } from '@angular/core';


class MockPostService {
  getAllPosts() {
    return of([
      { id: 1, title: 'Post 1', content: '', author: 'Author', authorEmail: '', draft: false, approved: true, createdDate: new Date() },
    ]);
  }
}
@Component({ template: '' })
class DummyComponent {}
class MockAuthService {
  getCurrentUser() {
    return 'Author';
  }
  isEditor() {
    return true;
  }
}

describe('PostListComponent', () => {
  let component: PostListComponent;
  let fixture: ComponentFixture<PostListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      
      imports: [PostListComponent, HttpClientTestingModule,
        RouterTestingModule.withRoutes([
          { path: 'posts', component: PostListComponent },
          { path: 'posts/new', component: DummyComponent },
          { path: 'posts/:id', component: DummyComponent },
          { path: '**', redirectTo: '' },
        ]),],
      providers: [
        { provide: PostService, useClass: MockPostService },
        { provide: AuthService, useClass: MockAuthService },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(PostListComponent);
    component = fixture.componentInstance;
  });

  it('should load posts on init', () => {
    component.ngOnInit();
    expect(component.publishedPosts.length).toBe(1);
  });

  it('should filter posts by text', () => {
    component.publishedPosts = [
      { id: 1, title: 'Test Post', content: 'Content', author: 'Author', authorEmail: 'test@gmail.com', draft: false, approved: true },
    ];
    component.filterText = 'Test';
    component.filterPosts();
    expect(component.filteredPublishedPosts.length).toBe(1); 
  });
  it('should show all posts when filter text is empty', () => {
    component.publishedPosts = [
      { id: 1, title: 'Post 1', content: 'Content 1', author: 'Author1', authorEmail: '', draft: false, approved: true },
      { id: 2, title: 'Post 2', content: 'Content 2', author: 'Author2', authorEmail: '', draft: false, approved: true },
    ];
    component.filterText = '';
    component.filterPosts();
    expect(component.filteredPublishedPosts.length).toBe(2);
  });
  
  it('should filter posts by date', () => {
    component.publishedPosts = [
      { id: 1, title: 'Post 1', content: 'Content 1', author: 'Author', authorEmail: 'test@gmail.com', createdDate: new Date('2025-01-12'), draft: false, approved: true },
      { id: 2, title: 'Post 2', content: 'Content 2', author: 'Author',  authorEmail: 'test@gmail.com', createdDate: new Date('2025-01-13'), draft: false, approved: true },
    ];
    component.filterDate = '2025-01-12';
    component.filterPosts();
    expect(component.filteredPublishedPosts.length).toBe(1);
    expect(component.filteredPublishedPosts[0].title).toBe('Post 1');
  });
  
  it('should load drafts for editor', () => {
    spyOn(component.authService, 'isEditor').and.returnValue(true);
    spyOn(component.authService, 'getCurrentUser').and.returnValue('EditorUser');
  
    const mockPosts = [
      { id: 1, title: 'Published Post', content: 'Published Content', author: 'AnotherUser', authorEmail: '', draft: false, approved: true },
      { id: 2, title: 'Published Post 2', content: 'Published Content 2', author: 'AnotherUser', authorEmail: '', draft: false, approved: true },
      { id: 3, title: 'Draft Post', content: 'Draft Content', author: 'EditorUser', authorEmail: 'test@gmail.com', draft: true, approved: false },
    ];
  
    spyOn(component.postService, 'getAllPosts').and.returnValue(of(mockPosts));
  
    component.loadPosts();
    expect(component.draftPosts.length).toBe(1);
    expect(component.draftPosts[0].author).toBe('EditorUser');
  });
  afterEach(() => {
    TestBed.resetTestingModule();
  });
});