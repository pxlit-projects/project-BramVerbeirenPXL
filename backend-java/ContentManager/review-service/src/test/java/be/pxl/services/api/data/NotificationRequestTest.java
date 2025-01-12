package be.pxl.services.api.data;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NotificationRequestTest {

    @Test
    void notificationRequestBuilder_setsAllFieldsCorrectly() {
        NotificationRequest notificationRequest = NotificationRequest.builder()
                .sender("Service")
                .to("user@example.com")
                .subject("Test Subject")
                .message("This is a test message")
                .build();

        assertEquals("Service", notificationRequest.getSender());
        assertEquals("user@example.com", notificationRequest.getTo());
        assertEquals("Test Subject", notificationRequest.getSubject());
        assertEquals("This is a test message", notificationRequest.getMessage());
    }

    @Test
    void notificationRequest_defaultConstructor_createsEmptyNotificationRequest() {
        NotificationRequest notificationRequest = new NotificationRequest();

        assertNull(notificationRequest.getSender());
        assertNull(notificationRequest.getTo());
        assertNull(notificationRequest.getSubject());
        assertNull(notificationRequest.getMessage());
    }
}
