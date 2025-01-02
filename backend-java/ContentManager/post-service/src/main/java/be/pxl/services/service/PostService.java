package be.pxl.services.service;

import be.pxl.services.api.data.NotificationRequest;
import be.pxl.services.api.data.PostRequest;
import be.pxl.services.client.NotificationClient;
import be.pxl.services.domain.Post;
import be.pxl.services.domain.specification.PostSpecification;
import be.pxl.services.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService implements IPostService{

    private PostRepository postRepository;
    private final NotificationClient notificationClient;

    @Autowired
    public PostService(PostRepository postRepository, NotificationClient notificationClient) {
        this.postRepository = postRepository;
        this.notificationClient = notificationClient;
    }
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Post getPostById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));
    }
    public Post createPost(PostRequest postRequest) {
        Post post = Post.builder()
                .title(postRequest.getTitle())
                .content(postRequest.getContent())
                .author(postRequest.getAuthor())
                .createdDate(LocalDate.now()).build();
        postRepository.save(post);
        NotificationRequest notificationRequest = NotificationRequest.builder()
                .message("Post created!")
                .sender(post.getAuthor()).build();
        notificationClient.sendNotification(notificationRequest);
        return post;
    }

    public Post saveDraft(PostRequest postRequest) {
        Post post = Post.builder()
                .title(postRequest.getTitle())
                .content(postRequest.getContent())
                .author(postRequest.getAuthor())
                .createdDate(LocalDate.now()).build();
        post.setDraft(true);
        return postRepository.save(post);
    }

    public Post updatePost(Long id, PostRequest postRequest) {
        return postRepository.findById(id)
                .map(existingPost -> {
                    existingPost.setTitle(postRequest.getTitle());
                    existingPost.setContent(postRequest.getContent());
                    existingPost.setAuthor(postRequest.getAuthor());
                    existingPost.setCreatedDate(LocalDate.now());
                    return postRepository.save(existingPost);
                })
                .orElseThrow(() -> new RuntimeException("Post not found"));
    }
    public List<Post> getPublishedPosts() {
        return postRepository.findByDraftFalse();
    }

    public List<Post> filterPosts(String content, String author, LocalDate date) {
        return postRepository.findAll(PostSpecification.filter(content, author, date));
    }

    public List<Post> getPendingPosts() {
        return postRepository.findByApprovedFalse();
    }
}
