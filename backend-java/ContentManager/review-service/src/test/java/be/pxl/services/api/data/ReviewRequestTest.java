package be.pxl.services.api.data;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReviewRequestTest {

    @Test
    void reviewRequestBuilder_setsAllFieldsCorrectly() {
        ReviewRequest reviewRequest = ReviewRequest.builder()
                .rejectionComment("This is a rejection reason")
                .build();

        assertEquals("This is a rejection reason", reviewRequest.getRejectionComment());
    }

    @Test
    void reviewRequest_defaultConstructor_createsEmptyReviewRequest() {
        ReviewRequest reviewRequest = new ReviewRequest();

        assertNull(reviewRequest.getRejectionComment());
    }
}
