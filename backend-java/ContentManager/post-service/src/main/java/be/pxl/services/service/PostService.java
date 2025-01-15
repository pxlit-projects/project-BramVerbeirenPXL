package be.pxl.services.service;

import be.pxl.services.api.data.NotificationRequest;
import be.pxl.services.api.data.PostRequest;
import be.pxl.services.api.data.ReviewRequest;
import be.pxl.services.client.NotificationClient;
import be.pxl.services.domain.Post;
import be.pxl.services.domain.specification.PostSpecification;
import be.pxl.services.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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
    public PostService(PostRepository postRepository, NotificationClient notificationClient, RabbitTemplate rabbitTemplate) {
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
        if(postRequest.getTitle().isEmpty()) {
            throw new IllegalArgumentException("Title must not be empty");
        } else if (postRequest.getAuthor() == null || postRequest.getAuthor().isEmpty() ) {
            throw new IllegalArgumentException("Author must not be empty");
        }
        Post post = Post.builder()
                .title(postRequest.getTitle())
                .content(postRequest.getContent())
                .authorEmail(postRequest.getAuthorEmail())
                .author(postRequest.getAuthor())
                .draft(postRequest.isDraft())
                .createdDate(LocalDate.now()).build();

        postRepository.save(post);
        NotificationRequest notificationRequest = NotificationRequest.builder()
                .message("Post created!")
                .sender(post.getAuthor()).build();
        notificationClient.sendNotification(notificationRequest);
        return post;
    }

    public Post saveDraft(PostRequest postRequest) {
        if(postRequest.getTitle().isEmpty()) {
            throw new IllegalArgumentException("Title must not be empty");
        } else if (postRequest.getAuthor() == null || postRequest.getAuthor().isEmpty() ) {
            throw new IllegalArgumentException("Author must not be null");
        }
        Post post = Post.builder()
                .title(postRequest.getTitle())
                .content(postRequest.getContent())
                .authorEmail(postRequest.getAuthorEmail())
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
                    existingPost.setAuthorEmail(postRequest.getAuthorEmail());
                    existingPost.setCreatedDate(LocalDate.now());
                    existingPost.setDraft(postRequest.isDraft());
                    return postRepository.save(existingPost);
                })
                .orElseThrow(() -> new RuntimeException("Post not found"));
    }
    public List<Post> getPublishedPosts() {
        return postRepository.findByDraftFalseAndApprovedTrue();
    }

    public List<Post> filterPosts(String content, String author, LocalDate date) {
        return postRepository.findAll(PostSpecification.filter(content, author, date));
    }

    public List<Post> getPendingPosts() {
        return postRepository.findByDraftFalseAndApprovedFalse();
    }
    @Override
    public Post updatePostStatus(Long id, ReviewRequest reviewRequest) {
        Post updatedPost = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        updatedPost.setApproved(reviewRequest.isApproved());

        // Voeg dit toe om het veld rejectionComment bij te werken
        if (!reviewRequest.isApproved()) {
            updatedPost.setRejectionComment(reviewRequest.getRejectionComment());
        }

        postRepository.save(updatedPost);

        String status = reviewRequest.isApproved() ? "goedgekeurd" : "afgewezen";
        String emailMessage = reviewRequest.isApproved()
                ? "Gefeliciteerd! Je post \"" + updatedPost.getTitle() + "\" is goedgekeurd."
                : "Helaas is je post \"" + updatedPost.getTitle() + "\" afgekeurd met reden: \"" + reviewRequest.getRejectionComment() + "\"";

        NotificationRequest notificationRequest = NotificationRequest.builder()
                .to(updatedPost.getAuthorEmail())  // Verzendadres van de auteur
                .message(emailMessage)
                .subject("Statusupdate voor je post: " + updatedPost.getTitle())
                .sender("Post Review Service")
                .build();
        notificationClient.sendNotification(notificationRequest);

        return updatedPost;
    }
    public void deletePost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found"));
        postRepository.delete(post);
    }
}
