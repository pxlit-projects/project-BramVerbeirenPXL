package be.pxl.services.client;

import be.pxl.services.api.data.NotificationRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

@SpringBootTest
class NotificationClientTest {

    @MockBean
    private NotificationClient notificationClient;

    @Test
    void sendNotification_succeeds() {
        NotificationRequest request = new NotificationRequest("Author", "receiver@test.com", "Subject", "Message content");

        doNothing().when(notificationClient).sendNotification(any(NotificationRequest.class));

        assertDoesNotThrow(() -> notificationClient.sendNotification(request));
    }
}
