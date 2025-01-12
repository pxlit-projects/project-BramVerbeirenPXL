package be.pxl.services.service;

import be.pxl.services.api.data.PostRequest;
import be.pxl.services.api.data.ReviewRequest;
import be.pxl.services.domain.Post;

import java.time.LocalDate;
import java.util.List;

public interface IPostService {
    List<Post> getAllPosts();

    Post getPostById(Long id);
    Post createPost(PostRequest postRequest);

    Post saveDraft(PostRequest postRequest);

    Post updatePost(Long id, PostRequest postRequest);
    List<Post> getPublishedPosts();

    List<Post> filterPosts(String content, String author, LocalDate date);
    List<Post> getPendingPosts();
    Post updatePostStatus(Long id, ReviewRequest reviewRequest);
    void deletePost(Long id);
}
