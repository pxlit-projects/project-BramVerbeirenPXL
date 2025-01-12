package be.pxl.services.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReviewTest {

    @Test
    void reviewBuilder_setsAllFieldsCorrectly() {
        Review review = Review.builder()
                .postId(1L)
                .approved(true)
                .rejectionComment("Test rejection")
                .build();

        assertEquals(1L, review.getPostId());
        assertTrue(review.isApproved());
        assertEquals("Test rejection", review.getRejectionComment());
    }

    @Test
    void review_equalsAndHashCode_worksCorrectly() {
        Review review1 = new Review(1L, 1L, true, "Test rejection");
        Review review2 = new Review(1L, 1L, true, "Test rejection");

        assertEquals(review1, review2);
        assertEquals(review1.hashCode(), review2.hashCode());
    }

    @Test
    void review_defaultConstructor_createsEmptyReview() {
        Review review = new Review();

        assertNull(review.getPostId());
        assertFalse(review.isApproved());
        assertNull(review.getRejectionComment());
    }
}
