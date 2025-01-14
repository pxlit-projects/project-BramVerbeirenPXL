
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { CommentSectionComponent } from './comment-section.component';
import { Component } from '@angular/core';
import { CommentService } from '../../services/comment.service';
import { AuthService } from '../../guards/auth.service';
import { of } from 'rxjs';

@Component({ template: '' })
class DummyComponent {}

class MockCommentService {
  getCommentsByPost(postId: number) {
    return of([{ id: 1, postId, author: 'User', content: 'Test comment' }]);
  }
  addComment(postId: number, comment: any) {
    return of({ id: 2, ...comment });
  }
  updateComment(commentId: number, updatedContent: string) {
    return of({ id: commentId, content: updatedContent });
  }
  deleteComment(commentId: number) {
    return of({});
  }
}

class MockAuthService {
  getCurrentUser() {
    return 'User';
  }
}

describe('CommentSectionComponent', () => {
  let component: CommentSectionComponent;
  let fixture: ComponentFixture<CommentSectionComponent>;
  let commentService: CommentService;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule],
      providers: [
        { provide: CommentService, useClass: MockCommentService },
        { provide: AuthService, useClass: MockAuthService },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(CommentSectionComponent);
    component = fixture.componentInstance;
    commentService = TestBed.inject(CommentService);
  });

  afterEach(() => {
    TestBed.resetTestingModule(); 
  });

  it('should start editing a comment', () => {
    const comment = { id: 1, postId: 1, content: 'Initial Comment', author: 'User', createdDate: new Date() };
    component.startEditComment(comment);
    expect(component.editingComment).toEqual(comment);
    expect(component.editedContent).toBe('Initial Comment');
  });
  
  it('should cancel editing a comment', () => {
    component.editingComment = { id: 1, postId: 1, content: 'Initial Comment', author: 'User', createdDate: new Date() };
    component.cancelEdit();
    expect(component.editingComment).toBeNull();
    expect(component.editedContent).toBe('');
  });
  
  it('should save edited comment', () => {
    const spy = spyOn(commentService, 'updateComment').and.returnValue(of({ postId: 1, id: 1, author: 'Bram', content: 'Updated Comment', createdDate: new Date() }));
    const comment = { id: 1, postId: 1, content: 'Initial Comment', author: 'User', createdDate: new Date() };
  
    component.startEditComment(comment);
    component.editedContent = 'Updated Comment';
    component.saveEdit(comment);
  
    expect(spy).toHaveBeenCalledWith(1, 'Updated Comment');
    expect(component.editingComment).toBeNull();
    expect(component.editedContent).toBe('');
  });
  
  it('should not save edited comment if content is empty', () => {
    const spy = spyOn(commentService, 'updateComment');
    const comment = { id: 1, postId: 1, content: 'Initial Comment', author: 'User', createdDate: new Date() };
  
    component.startEditComment(comment);
    component.editedContent = '';
    component.saveEdit(comment);
  
    expect(spy).not.toHaveBeenCalled();
  });
});