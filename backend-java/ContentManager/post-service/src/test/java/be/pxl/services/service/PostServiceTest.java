package be.pxl.services.service;

import be.pxl.services.api.data.NotificationRequest;
import be.pxl.services.api.data.PostRequest;
import be.pxl.services.client.NotificationClient;
import be.pxl.services.domain.Post;
import be.pxl.services.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private NotificationClient notificationClient;

    @InjectMocks
    private PostService postService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllPosts_success() {
        // Arrange
        Post post1 = new Post("Title1", "Content1", "Author1", "email1@example.com", LocalDate.now());
        Post post2 = new Post("Title2", "Content2", "Author2", "email2@example.com", LocalDate.now());
        when(postRepository.findAll()).thenReturn(List.of(post1, post2));

        // Act
        List<Post> result = postService.getAllPosts();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(postRepository, times(1)).findAll();
    }

    @Test
    void getPostById_success() {
        // Arrange
        Post post = new Post("Title", "Content", "Author", "email@example.com", LocalDate.now());
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        // Act
        Post result = postService.getPostById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("Title", result.getTitle());
        verify(postRepository, times(1)).findById(1L);
    }

    @Test
    void getPostById_notFound() {
        // Arrange
        when(postRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> postService.getPostById(1L));
        assertEquals("Post not found", exception.getMessage());
        verify(postRepository, times(1)).findById(1L);
    }

    @Test
    void createPost_success() {
        // Arrange
        PostRequest request = PostRequest.builder()
                .title("Title")
                .content("Content")
                .author("Author")
                .build();
        Post post = Post.builder()
                .id(1L)
                .title("Title")
                .content("Content")
                .author("Author")
                .createdDate(LocalDate.now())
                .build();
        when(postRepository.save(any(Post.class))).thenReturn(post);

        // Act
        Post result = postService.createPost(request);

        // Assert
        assertNotNull(result);
        assertEquals("Title", result.getTitle());
        verify(postRepository, times(1)).save(any(Post.class));
        verify(notificationClient, times(1)).sendNotification(any(NotificationRequest.class));
    }

    @Test
    void saveDraft_success() {
        // Arrange
        PostRequest request = PostRequest.builder()
                .title("Draft Title")
                .content("Draft Content")
                .author("Author")
                .build();
        Post draft = Post.builder()
                .id(1L)
                .title("Draft Title")
                .content("Draft Content")
                .author("Author")
                .createdDate(LocalDate.now())
                .draft(true)
                .build();
        when(postRepository.save(any(Post.class))).thenReturn(draft);

        // Act
        Post result = postService.saveDraft(request);

        // Assert
        assertNotNull(result);
        assertTrue(result.isDraft());
        assertEquals("Draft Title", result.getTitle());
        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    void updatePost_success() {
        // Arrange
        Post existingPost = Post.builder()
                .id(1L)
                .title("Old Title")
                .content("Old Content")
                .author("Old Author")
                .createdDate(LocalDate.now())
                .build();
        PostRequest request = PostRequest.builder()
                .title("New Title")
                .content("New Content")
                .author("New Author")
                .build();
        when(postRepository.findById(1L)).thenReturn(Optional.of(existingPost));
        when(postRepository.save(any(Post.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Post result = postService.updatePost(1L, request);

        // Assert
        assertNotNull(result);
        assertEquals("New Title", result.getTitle());
        verify(postRepository, times(1)).findById(1L);
        verify(postRepository, times(1)).save(existingPost);
    }

    @Test
    void updatePost_notFound() {
        // Arrange
        PostRequest request = PostRequest.builder()
                .title("Title")
                .content("Content")
                .author("Author")
                .build();
        when(postRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> postService.updatePost(1L, request));
        assertEquals("Post not found", exception.getMessage());
        verify(postRepository, times(1)).findById(1L);
        verify(postRepository, never()).save(any(Post.class));
    }

    @Test
    void getPublishedPosts_success() {
        // Arrange
        Post post = Post.builder()
                .id(1L)
                .title("Published Post")
                .draft(false)
                .build();
        when(postRepository.findByDraftFalse()).thenReturn(List.of(post));

        // Act
        List<Post> result = postService.getPublishedPosts();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertFalse(result.get(0).isDraft());
        verify(postRepository, times(1)).findByDraftFalse();
    }

    @Test
    void getPendingPosts_success() {
        // Arrange
        Post post = Post.builder()
                .id(1L)
                .title("Pending Post")
                .approved(false)
                .build();
        when(postRepository.findByApprovedFalse()).thenReturn(List.of(post));

        // Act
        List<Post> result = postService.getPendingPosts();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertFalse(result.get(0).isApproved());
        verify(postRepository, times(1)).findByApprovedFalse();
    }
}
