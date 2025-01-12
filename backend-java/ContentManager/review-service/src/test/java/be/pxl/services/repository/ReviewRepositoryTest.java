package be.pxl.services.repository;

import be.pxl.services.PostServiceApplication;
import be.pxl.services.ReviewServiceApplication;
import be.pxl.services.domain.Review;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ContextConfiguration(classes = ReviewServiceApplication.class)
class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @BeforeEach
    void setup() {
        reviewRepository.deleteAll();
    }

    @Test
    void findByApprovedFalse_returnsPendingReviews() {
        Review review = Review.builder().postId(1L).approved(false).build();
        reviewRepository.save(review);

        List<Review> pendingReviews = reviewRepository.findByApprovedFalse();

        assertEquals(1, pendingReviews.size());
        assertFalse(pendingReviews.get(0).isApproved());
    }

    @Test
    void findByPostId_returnsReview() {
        Review review = Review.builder().postId(1L).approved(true).build();
        reviewRepository.save(review);

        Optional<Review> result = reviewRepository.findByPostId(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getPostId());
    }
}
