package be.pxl.services.service;

import be.pxl.services.api.data.NotificationRequest;
import be.pxl.services.api.data.PostRequest;
import be.pxl.services.api.data.ReviewRequest;
import be.pxl.services.client.NotificationClient;
import be.pxl.services.client.PostClient;
import be.pxl.services.domain.Post;
import be.pxl.services.domain.Review;
import be.pxl.services.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private NotificationClient notificationClient;

    @Mock
    private PostClient postClient;

    @InjectMocks
    private ReviewService reviewService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getPendingReviews_returnsPosts() {
        List<Post> posts = List.of(Post.builder().title("Test Post").build());
        when(postClient.getPendingPosts()).thenReturn(posts);

        List<Post> result = reviewService.getPendingReviews();

        assertEquals(1, result.size());
        assertEquals("Test Post", result.get(0).getTitle());
    }

    @Test
    void approvePost_savesReviewAndSendsNotification() {
        PostRequest postRequest = PostRequest.builder().approved(true).build();
        Review review = Review.builder().postId(1L).approved(true).build();

        when(reviewRepository.save(any(Review.class))).thenReturn(review);

        Review result = reviewService.approvePost(1L, any(ReviewRequest.class));

        verify(postClient).updatePostStatus(eq(1L), any(ReviewRequest.class));
        verify(notificationClient).sendNotification(any(NotificationRequest.class));

        assertTrue(result.isApproved());
        assertEquals(1L, result.getPostId());
    }

    @Test
    void rejectPost_savesRejectedReviewAndSendsNotification() {
        ReviewRequest reviewRequest = ReviewRequest.builder().rejectionComment("Rejected due to content").build();

        Review result = reviewService.rejectPost(1L, reviewRequest);

        verify(postClient).updatePostStatus(eq(1L), any(ReviewRequest.class));
        verify(notificationClient).sendNotification(any(NotificationRequest.class));

        assertFalse(result.isApproved());
        assertEquals("Rejected due to content", result.getRejectionComment());
    }
}
