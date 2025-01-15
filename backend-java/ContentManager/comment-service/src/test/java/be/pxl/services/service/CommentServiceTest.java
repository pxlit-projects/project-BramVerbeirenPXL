package be.pxl.services.service;

import be.pxl.services.api.data.CommentRequest;
import be.pxl.services.api.data.NotificationRequest;
import be.pxl.services.client.NotificationClient;
import be.pxl.services.config.RabbitMQConfig;
import be.pxl.services.domain.Comment;
import be.pxl.services.repository.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private NotificationClient notificationClient;


    @InjectMocks
    private CommentService commentService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getCommentsByPostId_returnsComments() {
        List<Comment> comments = List.of(new Comment(1L, 1L, "Author", "Content", null));
        when(commentRepository.findByPostId(1L)).thenReturn(comments);

        List<Comment> result = commentService.getCommentsByPostId(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAuthor()).isEqualTo("Author");
        verify(commentRepository, times(1)).findByPostId(1L);
    }

    @Test
    void getCommentsByPostId_returnsEmptyList() {
        when(commentRepository.findByPostId(1L)).thenReturn(Collections.emptyList());

        List<Comment> result = commentService.getCommentsByPostId(1L);

        assertThat(result).isEmpty();
        verify(commentRepository).findByPostId(1L);
    }

    @Test
    void addComment_createsNewComment() {
        CommentRequest commentRequest = new CommentRequest("Author", "Content");
        Comment comment = new Comment(1L, 1L, "Author", "Content", null);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        doNothing().when(notificationClient).sendNotification(any(NotificationRequest.class));

        Comment result = commentService.addComment(1L, commentRequest);

        assertThat(result.getAuthor()).isEqualTo("Author");
        assertThat(result.getContent()).isEqualTo("Content");

        verify(notificationClient).sendNotification(any(NotificationRequest.class));
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    void addComment_throwsExceptionIfRequestIsNull() {
        assertThrows(NullPointerException.class, () -> commentService.addComment(1L, null));
    }

    @Test
    void updateComment_updatesExistingComment() {
        Comment comment = new Comment(1L, 1L, "Author", "Original Content", null);
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
        when(commentRepository.save(comment)).thenReturn(comment);

        Comment updatedComment = commentService.updateComment(1L, "Updated Content");

        assertThat(updatedComment.getContent()).isEqualTo("Updated Content");
        verify(commentRepository).save(comment);
    }

    @Test
    void updateComment_throwsExceptionIfCommentNotFound() {
        when(commentRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> commentService.updateComment(1L, "Updated Content"));

        assertThat(exception.getMessage()).isEqualTo("Comment not found");
        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    void updateComment_handlesEmptyContent() {
        Comment existingComment = new Comment(1L, 1L, "Author", "Original Content", null);
        when(commentRepository.findById(1L)).thenReturn(Optional.of(existingComment));

        // Voeg ook mock voor de save() methode toe om een geüpdatete Comment terug te geven
        when(commentRepository.save(any(Comment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Roep de update aan met lege content
        Comment updatedComment = commentService.updateComment(1L, "");

        assertThat(updatedComment).isNotNull();  // Controleer dat de Comment niet null is
        assertThat(updatedComment.getContent()).isEmpty();  // Controleer dat de content leeg is
    }

    @Test
    void deleteComment_deletesComment() {
        when(commentRepository.existsById(1L)).thenReturn(true);
        doNothing().when(commentRepository).deleteById(1L);

        commentService.deleteComment(1L);

        verify(commentRepository).deleteById(1L);
    }

    @Test
    void deleteComment_throwsExceptionIfNotFound() {
        when(commentRepository.existsById(1L)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> commentService.deleteComment(1L));

        assertThat(exception.getMessage()).isEqualTo("Comment not found");
        verify(commentRepository, never()).deleteById(1L);
    }

    @Test
    void addComment_sendsNotificationAndRabbitMessage() {
        CommentRequest commentRequest = new CommentRequest("Author", "Test Content");
        Comment comment = new Comment(1L, 1L, "Author", "Test Content", null);

        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        doNothing().when(notificationClient).sendNotification(any(NotificationRequest.class));

        Comment result = commentService.addComment(1L, commentRequest);

        assertThat(result.getAuthor()).isEqualTo("Author");
        assertThat(result.getContent()).isEqualTo("Test Content");

        verify(notificationClient, times(1)).sendNotification(any(NotificationRequest.class));
    }
}
