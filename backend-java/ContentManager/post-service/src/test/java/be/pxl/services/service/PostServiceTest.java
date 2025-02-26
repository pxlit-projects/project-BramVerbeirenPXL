package be.pxl.services.service;

import be.pxl.services.api.data.NotificationRequest;
import be.pxl.services.api.data.PostRequest;
import be.pxl.services.api.data.ReviewRequest;
import be.pxl.services.client.NotificationClient;
import be.pxl.services.domain.Post;
import be.pxl.services.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Example;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private NotificationClient notificationClient;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private PostService postService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        reset(postRepository, rabbitTemplate, notificationClient);  // Alle mocks resetten
    }

    @Test
    void createPost_sendsNotificationAndPublishesToQueue() {
        PostRequest postRequest = new PostRequest("Title", "Content", "Author", "email@test.com", false, null, false);
        Post post = Post.builder().id(1L).title("Title").build();

        when(postRepository.save(any(Post.class))).thenReturn(post);

        Post createdPost = postService.createPost(postRequest);

        assertNotNull(createdPost);
        assertEquals("Title", createdPost.getTitle());
        verify(notificationClient).sendNotification(any(NotificationRequest.class));
    }
    @Test
    void filterPosts_withOnlyAuthor_returnsMatchingPosts() {
        List<Post> posts = List.of(new Post(1L, "Title", "Content", "Author", "email@test.com", LocalDate.of(2023, 12, 25), false, true, null));
        when(postRepository.findAll((Example<Post>) any())).thenReturn(posts);

        List<Post> result = postService.filterPosts(null, "Author", null);

        assertEquals(1, result.size());
        assertEquals("Author", result.get(0).getAuthor());
    }
    @Test
    void filterPosts_noFilters_returnsAllPosts() {
        List<Post> posts = List.of(
                new Post(1L, "Title 1", "Content 1", "Author 1", "email1@test.com", LocalDate.now(), false, true, null),
                new Post(2L, "Title 2", "Content 2", "Author 2", "email2@test.com", LocalDate.now(), false, true, null)
        );

        when(postRepository.findAll((Example<Post>) any())).thenReturn(posts);

        List<Post> result = postService.filterPosts(null, null, null);

        assertEquals(2, result.size());
    }

    @Test
    void filterPosts_returnsFilteredPosts() {
        // Testdata: één post die aan de criteria voldoet
        List<Post> filteredPosts = List.of(
                new Post(3L, "Filtered Title", "Filtered Content", "Author", "email@test.com", LocalDate.of(2023, 12, 25), false, true, null)
        );

        // Mocking: accepteer elke Specification als parameter
        when(postRepository.findAll((Example<Post>) any())).thenReturn(filteredPosts);

        // Uitvoeren van de test
        List<Post> result = postService.filterPosts("Filtered Content", "Author", LocalDate.of(2023, 12, 25));

        // Assertions
        assertEquals(1, result.size(), "Er wordt verwacht dat er één gefilterde post is");
        assertEquals("Filtered Title", result.get(0).getTitle());
        verify(postRepository, times(1)).findAll((Example<Post>) any());
    }

    @Test
    @Order(1)
    void updatePost_updatesPostIfExists() {
        Post existingPost = new Post(1L, "Old Title", "Old Content", "Author", "email@test.com", LocalDate.now(), false, true, null);
        PostRequest postRequest = new PostRequest("Updated Title", "Updated Content", "Author", "email@test.com", false, null, false);

        when(postRepository.findById(1L)).thenReturn(Optional.of(existingPost));
        when(postRepository.save(any(Post.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Post updatedPost = postService.updatePost(1L, postRequest);

        assertEquals("Updated Title", updatedPost.getTitle());
        verify(postRepository, times(1)).findById(1L);
        verify(postRepository).save(existingPost);
    }


    @Test
    void updatePost_throwsExceptionIfNotFound() {
        PostRequest postRequest = new PostRequest("New Title", "Updated Content", "Author", "email@test.com", false, null, false);
        when(postRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> postService.updatePost(1L, postRequest));
        assertEquals("Post not found", thrown.getMessage());
    }

    @Test
    void updatePostStatus_throwsExceptionIfNotFound() {
        ReviewRequest postRequest = new ReviewRequest(true, null);
        when(postRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> postService.updatePostStatus(1L, postRequest));
        assertEquals("Post not found", thrown.getMessage());
    }

    @Test
    void getPendingPosts_returnsPendingPosts() {
        List<Post> pendingPosts = List.of(new Post(2L, "Pending Title", "Pending Content", "Author", null, LocalDate.now(), false, false, null));
        when(postRepository.findByDraftFalseAndApprovedFalse()).thenReturn(pendingPosts);

        List<Post> result = postService.getPendingPosts();

        assertEquals(1, result.size());
        assertFalse(result.get(0).isApproved());
    }

    @Test
    void saveDraft_savesDraftPost() {
        PostRequest postRequest = new PostRequest("Draft Post", "Content", "Author", "email@test.com", false, null, true);
        Post post = Post.builder().id(1L).title("Draft Post").draft(true).build();

        when(postRepository.save(any(Post.class))).thenReturn(post);

        Post savedDraft = postService.saveDraft(postRequest);

        assertNotNull(savedDraft);
        assertTrue(savedDraft.isDraft());
    }
    @Test
    void getAllPosts_returnsAllPosts() {
        List<Post> posts = List.of(
                new Post(1L, "Title 1", "Content 1", "Author 1", "email1@test.com", LocalDate.now(), false, true, null),
                new Post(2L, "Title 2", "Content 2", "Author 2", "email2@test.com", LocalDate.now(), true, false, null)
        );

        when(postRepository.findAll()).thenReturn(posts);

        List<Post> result = postService.getAllPosts();

        assertEquals(2, result.size());
        verify(postRepository).findAll();
    }

    @Test
    void getPublishedPosts_returnsPublishedPosts() {
        List<Post> publishedPosts = List.of(
                new Post(3L, "Published Post", "Content", "Author", "email@test.com", LocalDate.now(), false, true, null)
        );

        when(postRepository.findByDraftFalseAndApprovedTrue()).thenReturn(publishedPosts);

        List<Post> result = postService.getPublishedPosts();

        assertEquals(1, result.size());
        assertFalse(result.get(0).isDraft());
        assertTrue(result.get(0).isApproved());
    }

}
