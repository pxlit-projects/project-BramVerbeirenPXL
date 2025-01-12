package be.pxl.services.api.controller;

import be.pxl.services.api.data.ReviewRequest;
import be.pxl.services.domain.Post;
import be.pxl.services.domain.Review;
import be.pxl.services.service.IReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ReviewControllerTest {

    @Mock
    private IReviewService reviewService;

    @InjectMocks
    private ReviewController reviewController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getPendingReviews_returnsListOfPendingReviews() {
        List<Post> pendingPosts = List.of(new Post(1L, "Post Title", "Post Content", "Author", "author@test.com", null, false, false, null));
        when(reviewService.getPendingReviews()).thenReturn(pendingPosts);

        ResponseEntity<List<Post>> response = reviewController.getPendingReviews();

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotEmpty();
        assertThat(response.getBody().size()).isEqualTo(1);
        verify(reviewService).getPendingReviews();
    }

    @Test
    void getPendingReviews_returnsNoContent() {
        when(reviewService.getPendingReviews()).thenReturn(Collections.emptyList());

        ResponseEntity<List<Post>> response = reviewController.getPendingReviews();

        assertThat(response.getStatusCode().value()).isEqualTo(204);
        assertThat(response.getBody()).isNull();
        verify(reviewService).getPendingReviews();
    }

    @Test
    void approvePost_succeeds() {
        Review review = new Review(1L, 1L, true, null);
        ReviewRequest reviewRequest = new ReviewRequest(true, null);
        when(reviewService.approvePost(1L, reviewRequest)).thenReturn(review);

        ResponseEntity<Review> response = reviewController.approvePost(1L, reviewRequest);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody().isApproved()).isTrue();
        verify(reviewService).approvePost(1L, reviewRequest);
    }

    @Test
    void approvePost_failsWithNotFound() {
        ReviewRequest reviewRequest = new ReviewRequest(true, null);
        when(reviewService.approvePost(1L, reviewRequest)).thenThrow(new RuntimeException("Post not found"));

        try {
            reviewController.approvePost(1L, reviewRequest);
        } catch (RuntimeException e) {
            assertThat(e.getMessage()).isEqualTo("Post not found");
        }

        verify(reviewService).approvePost(1L, reviewRequest);
    }

    @Test
    void rejectPost_succeeds() {
        Review review = new Review(1L, 1L, false, "Rejected for quality reasons");
        when(reviewService.rejectPost(1L, new ReviewRequest(false, "Rejected for quality reasons"))).thenReturn(review);

        ResponseEntity<Review> response = reviewController.rejectPost(1L, new ReviewRequest(false, "Rejected for quality reasons"));

        assertThat(response.getBody().getRejectionComment()).isEqualTo("Rejected for quality reasons");
        assertThat(response.getBody().isApproved()).isFalse();
        verify(reviewService).rejectPost(eq(1L), any(ReviewRequest.class));
    }

    @Test
    void rejectPost_failsWithInvalidComment() {
        when(reviewService.rejectPost(1L, new ReviewRequest(false, ""))).thenThrow(new IllegalArgumentException("Rejection comment cannot be empty"));

        try {
            reviewController.rejectPost(1L, new ReviewRequest(false, ""));
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo("Rejection comment cannot be empty");
        }

        verify(reviewService).rejectPost(eq(1L), any(ReviewRequest.class));
    }

    @Test
    void rejectPost_handlesNullReviewRequest() {
        try {
            reviewController.rejectPost(1L, null);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(NullPointerException.class);
        }
    }
}