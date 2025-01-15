package be.pxl.services.service;


import be.pxl.services.api.data.NotificationRequest;
import be.pxl.services.api.data.PostRequest;
import be.pxl.services.api.data.ReviewRequest;
import be.pxl.services.client.NotificationClient;
import be.pxl.services.client.PostClient;
import be.pxl.services.domain.Post;
import be.pxl.services.domain.Review;
import be.pxl.services.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService implements IReviewService {

    private final ReviewRepository reviewRepository;
    private final NotificationClient notificationClient;
    private final PostClient postClient;

    @Override
    public List<Post> getPendingReviews() {
        // Haalt alle pending posts op via de PostService
        return postClient.getPendingPosts();
    }

    @Override
    public Review approvePost(Long postId, ReviewRequest reviewRequest) {
        postClient.updatePostStatus(postId, reviewRequest);
        Review review = Review.builder()
                .postId(postId)
                .approved(true)
                .build();
        reviewRepository.save(review);
        NotificationRequest notificationRequest = NotificationRequest.builder()
                .sender("Review Service")
                .to("author@example.com")
                .subject("Post goedgekeurd!")
                .message("Je post met ID " + postId + " is goedgekeurd.")
                .build();
        notificationClient.sendNotification(notificationRequest);
        return review;
    }

    @Override
    public Review rejectPost(Long postId, ReviewRequest reviewRequest) {
        // Update de status van de post via PostService
        PostRequest postRequest = PostRequest.builder()
                .approved(false)
                .rejectionComment(reviewRequest.getRejectionComment())
                .build();
        postClient.updatePostStatus(postId, reviewRequest);

        // Maak en sla de review op
        Review review = Review.builder()
                .postId(postId)
                .approved(false)
                .rejectionComment(reviewRequest.getRejectionComment())
                .build();
        reviewRepository.save(review);

        // Verstuur notificatie bij afwijzing
        NotificationRequest notificationRequest = NotificationRequest.builder()
                .sender("Review Service")
                .to("author@example.com")  // Simulatie van auteur-email
                .subject("Post afgewezen")
                .message("Je post met ID " + postId + " is afgewezen. Opmerking: " + reviewRequest.getRejectionComment())
                .build();
        notificationClient.sendNotification(notificationRequest);

        return review;
    }
}