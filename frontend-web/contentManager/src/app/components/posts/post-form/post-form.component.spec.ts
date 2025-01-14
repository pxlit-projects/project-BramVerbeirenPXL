import { ComponentFixture, TestBed } from '@angular/core/testing';
import { PostFormComponent } from './post-form.component';
import { PostService } from '../../../services/post.service';
import { AuthService } from '../../../guards/auth.service';
import { RouterTestingModule } from '@angular/router/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { of } from 'rxjs';

describe('PostFormComponent', () => {
  let component: PostFormComponent;
  let fixture: ComponentFixture<PostFormComponent>;
  let postService: jasmine.SpyObj<PostService>;

  beforeEach(async () => {
    postService = jasmine.createSpyObj('PostService', ['createPost']);

    await TestBed.configureTestingModule({
      imports: [PostFormComponent, ReactiveFormsModule, RouterTestingModule], 
      providers: [{ provide: PostService, useValue: postService }],
      teardown: {destroyAfterEach: false}
    }).compileComponents();

    fixture = TestBed.createComponent(PostFormComponent);
    component = fixture.componentInstance;
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should submit the post', () => {
    postService.createPost.and.returnValue(of({ id: 1, title: 'Test Post', content: 'Content', author: 'User', authorEmail: 'test@example.com', draft: true, approved: false }));
    component.postForm.setValue({ title: 'Title', content: 'Content', author: 'Author', authorEmail: 'test@example.com' });

    component.submitPost(false);
    expect(postService.createPost).toHaveBeenCalled();
  });
  it('should show error for invalid email', () => {
    component.postForm.setValue({ title: 'Title', content: 'Content', author: 'Author', authorEmail: 'invalid-email' });
    expect(component.postForm.valid).toBeFalse();
  });
  
  it('should save post as draft', () => {
    postService.createPost.and.returnValue(of({ id: 1, title: 'Draft Post', content: 'Draft Content', author: 'Author', authorEmail: 'author@example.com', draft: true, approved: false }));
    component.postForm.setValue({ title: 'Draft Title', content: 'Draft Content', author: 'Author', authorEmail: 'author@example.com' });
  
    component.saveAsDraft();
    expect(postService.createPost).toHaveBeenCalledWith(jasmine.objectContaining({ draft: true }));
  });
  
  it('should alert when form is empty', () => {
    spyOn(window, 'alert');
    component.postForm.setValue({ title: '', content: '', author: '', authorEmail: '' });
    component.onSubmit();
    expect(window.alert).toHaveBeenCalledWith('Vul alle verplichte velden in.');
  });
  afterEach(() => {
    TestBed.resetTestingModule();
  });
});
