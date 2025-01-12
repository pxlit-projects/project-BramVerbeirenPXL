package be.pxl.services.api.controller;

import be.pxl.services.controller.NotificationController;
import be.pxl.services.domain.Notification;
import be.pxl.services.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class NotificationControllerTest {

    private MockMvc mockMvc;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private NotificationController notificationController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(notificationController).build();
    }

    @Test
    void sendMessage_sendsNotification() throws Exception {
        Notification notification = Notification.builder()
                .id(1L)
                .sender("System")
                .to("user@example.com")
                .subject("Test subject")
                .message("This is a test message")
                .build();

        mockMvc.perform(post("/notification")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "sender": "System",
                                  "to": "user@test.com",
                                  "subject": "Test Subject",
                                  "message": "This is a test message."
                                }
                                """))
                .andExpect(status().isCreated());

        verify(notificationService, times(1)).sendMessage(any(Notification.class));
    }
}