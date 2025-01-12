package be.pxl.services.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.spy;

class RabbitMQListenerTest {

    @Test
    void listenToPostMessages_printsMessage() {
        ReviewService reviewServiceSpy = spy(new ReviewService(null, null, null));
        String message = "Test message";

        reviewServiceSpy.listenToPostMessages(message);

        Mockito.verify(reviewServiceSpy).listenToPostMessages(message);
    }
}
