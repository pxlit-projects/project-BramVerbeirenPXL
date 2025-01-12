package be.pxl.services.service;

import be.pxl.services.api.data.CommentRequest;
import be.pxl.services.domain.Comment;

import java.util.List;

public interface ICommentService {

    void deleteComment(Long commentId);

    Comment updateComment(Long commentId, String content);

    Comment addComment(Long postId, CommentRequest commentRequest);

    List<Comment> getCommentsByPostId(Long postId);
}
