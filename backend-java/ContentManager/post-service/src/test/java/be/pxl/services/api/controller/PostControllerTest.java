package be.pxl.services.api.controller;

import be.pxl.services.api.data.PostRequest;
import be.pxl.services.api.data.ReviewRequest;
import be.pxl.services.domain.Post;
import be.pxl.services.service.IPostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PostControllerTest {

    @Mock
    private IPostService postService;

    @InjectMocks
    private PostController postController;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(postController)
                .setControllerAdvice(new GlobalExceptionHandler())  // Voeg exception handler toe
                .build();
    }

    @Test
    void getAllPosts_returnsListOfPosts() {
        List<Post> posts = List.of(new Post(1L, "Test Title", "Test Content", "Author", null, null, false, true, null));
        when(postService.getAllPosts()).thenReturn(posts);

        ResponseEntity<List<Post>> response = postController.getAllPosts();

        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).hasSize(1);
        verify(postService, times(1)).getAllPosts();
    }

    @Test
    void getAllPosts_returnsEmptyList() {
        when(postService.getAllPosts()).thenReturn(Collections.emptyList());

        ResponseEntity<List<Post>> response = postController.getAllPosts();

        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isEmpty();
        verify(postService, times(1)).getAllPosts();
    }

    @Test
    void createPost_createsNewPost() {
        PostRequest postRequest = new PostRequest("Title", "Content", "Author", "email@test.com", false, null, false);
        Post post = Post.builder().id(1L).title("Title").build();
        when(postService.createPost(postRequest)).thenReturn(post);

        ResponseEntity<Post> response = postController.createPost(postRequest);

        assertThat(response.getStatusCode()).isEqualTo(CREATED);
        assertThat(response.getBody()).isNotNull();
        verify(postService).createPost(postRequest);
    }

    @Test
    void updatePost_updatesExistingPost() {
        PostRequest postRequest = new PostRequest("Updated Title", "Updated Content", "Author", "email@test.com", false, null, false);
        Post updatedPost = Post.builder().id(1L).title("Updated Title").build();
        when(postService.updatePost(1L, postRequest)).thenReturn(updatedPost);

        ResponseEntity<Post> response = postController.updatePost(1L, postRequest);

        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody().getTitle()).isEqualTo("Updated Title");
        verify(postService).updatePost(1L, postRequest);
    }

    @Test
    void getPostById_returnsPost() {
        Post post = new Post(1L, "Title", "Content", "Author", null, null, false, true, null);
        when(postService.getPostById(1L)).thenReturn(post);

        ResponseEntity<Post> response = postController.getPostById(1L);

        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody().getId()).isEqualTo(1L);
        verify(postService).getPostById(1L);
    }

    @Test
    void getPostById_notFound() throws Exception {
        when(postService.getPostById(1L)).thenReturn(null);

        mockMvc.perform(get("/api/posts/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getPublishedPosts_returnsPublishedPosts() {
        List<Post> posts = List.of(new Post(1L, "Published Title", "Content", "Author", null, null, true, true, null));
        when(postService.getPublishedPosts()).thenReturn(posts);

        ResponseEntity<List<Post>> response = postController.getPublishedPosts();

        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).hasSize(1);
        verify(postService, times(1)).getPublishedPosts();
    }

    @Test
    void getPublishedPosts_noContent() {
        when(postService.getPublishedPosts()).thenReturn(Collections.emptyList());

        ResponseEntity<List<Post>> response = postController.getPublishedPosts();

        assertThat(response.getStatusCode()).isEqualTo(NO_CONTENT);
    }

    @Test
    void filterPosts_returnsFilteredPosts() {
        List<Post> filteredPosts = List.of(new Post(1L, "Filtered Title", "Filtered Content", "Author", null, null, false, true, null));
        when(postService.filterPosts("Filtered", "Author", null)).thenReturn(filteredPosts);

        ResponseEntity<List<Post>> response = postController.filterPosts("Filtered", "Author", null);

        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).hasSize(1);
        verify(postService).filterPosts("Filtered", "Author", null);
    }

    @Test
    void filterPosts_noMatches() {
        when(postService.filterPosts("Non-existent", "Unknown", null)).thenReturn(Collections.emptyList());

        ResponseEntity<List<Post>> response = postController.filterPosts("Non-existent", "Unknown", null);

        assertThat(response.getStatusCode()).isEqualTo(NO_CONTENT);
    }

    @Test
    void getPendingPosts_returnsPendingPosts() {
        List<Post> posts = List.of(new Post(1L, "Pending Title", "Pending Content", "Author", null, null, false, false, null));
        when(postService.getPendingPosts()).thenReturn(posts);

        ResponseEntity<List<Post>> response = postController.getPendingPosts();

        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).hasSize(1);
    }

    @Test
    void updatePostStatus_updatesPostStatus() {
        ReviewRequest reviewRequest = new ReviewRequest(true, "approved");
        Post updatedPost = new Post(1L, "Updated Title", "Updated Content", "Author", null, null, true, true, null);
        when(postService.updatePostStatus(1L, reviewRequest)).thenReturn(updatedPost);

        ResponseEntity<Post> response = postController.updatePostStatus(1L, reviewRequest);

        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody().getTitle()).isEqualTo("Updated Title");
        verify(postService).updatePostStatus(1L, reviewRequest);
    }
    @Test
    void getPendingPosts_noContent() {
        when(postService.getPendingPosts()).thenReturn(Collections.emptyList());

        ResponseEntity<List<Post>> response = postController.getPendingPosts();

        assertThat(response.getStatusCode()).isEqualTo(NO_CONTENT);
        assertThat(response.getBody()).isNull(); // No body when status is 204
    }
    @Test
    void saveDraft_createsNewDraft() {
        PostRequest postRequest = new PostRequest("Draft Title", "Draft Content", "Author", "author@test.com", false, null, false);
        Post draft = new Post(1L, "Draft Title", "Draft Content", "Author", null, null, false, false, null);
        when(postService.saveDraft(postRequest)).thenReturn(draft);

        ResponseEntity<Post> response = postController.saveDraft(postRequest);

        assertThat(response.getStatusCode()).isEqualTo(CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo("Draft Title");
        verify(postService).saveDraft(postRequest);
    }
    @Test
    void updatePostStatus_notFound() throws Exception {
        ReviewRequest postRequest = new ReviewRequest( true, "approved");
        when(postService.updatePostStatus(1L, postRequest)).thenReturn(null);

        mockMvc.perform(put("/api/posts/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(postRequest)))
                .andExpect(status().isNotFound());
    }
    @Test
    void filterPosts_withDateFilter() {
        LocalDate date = LocalDate.of(2023, 12, 25);
        List<Post> filteredPosts = List.of(new Post(2L, "Christmas Title", "Content", "Santa", "email", date, false, true, null));
        when(postService.filterPosts(null, "Santa", date)).thenReturn(filteredPosts);

        ResponseEntity<List<Post>> response = postController.filterPosts(null, "Santa", date);

        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).getTitle()).isEqualTo("Christmas Title");
    }
    @Test
    void createPost_withInvalidRequest() throws Exception {
        PostRequest postRequest = new PostRequest(null, "Content without title", "Author", "author@test.com", false, null, false);

        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(postRequest)))
                .andDo(print())  // Voegt gedetailleerde log toe
                .andExpect(status().isBadRequest());
    }
    @Test
    void updatePost_notFound() throws Exception {
        PostRequest postRequest = new PostRequest("Non-existent", "No Content", "Unknown Author", "unknown@test.com", false, null, false);
        when(postService.updatePost(anyLong(), any(PostRequest.class))).thenReturn(null);

        mockMvc.perform(put("/api/posts/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(postRequest)))
                .andExpect(status().isNotFound());  // Verwacht NOT_FOUND
    }


}
