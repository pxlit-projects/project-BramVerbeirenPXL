package be.pxl.services.service;

import be.pxl.services.api.data.CommentRequest;
import be.pxl.services.api.data.NotificationRequest;
import be.pxl.services.client.NotificationClient;
import be.pxl.services.config.RabbitMQConfig;
import be.pxl.services.domain.Comment;
import be.pxl.services.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService implements ICommentService{
    private final CommentRepository commentRepository;
    private final NotificationClient notificationClient;

    public List<Comment> getCommentsByPostId(Long postId) {
        return commentRepository.findByPostId(postId);
    }

    public Comment addComment(Long postId, CommentRequest commentRequest) {
        Comment comment = Comment.builder()
                .content(commentRequest.getContent())
                .author(commentRequest.getAuthor())
                .createdDate(LocalDateTime.now()).build();
        comment.setPostId(postId);
        NotificationRequest notificationRequest = NotificationRequest.builder()
                .message("Comment posted!")
                .sender(comment.getAuthor()).build();
        notificationClient.sendNotification(notificationRequest);

        return commentRepository.save(comment);
    }

    public Comment updateComment(Long commentId, String content) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        if (optionalComment.isPresent()) {
            Comment comment = optionalComment.get();
            comment.setContent(content);
            return commentRepository.save(comment);
        }
        throw new RuntimeException("Comment not found");
    }

    public void deleteComment(Long commentId) {
        if (commentRepository.existsById(commentId)) {
            commentRepository.deleteById(commentId);
        } else {
            throw new RuntimeException("Comment not found");
        }
    }
}
