package be.pxl.services.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NotificationTest {

    @Test
    void notificationConstructor_setsFieldsCorrectly() {
        Notification notification = new Notification(1L, "System", "user@test.com", "Test Subject", "This is a test message.");

        assertThat(notification.getSender()).isEqualTo("System");
        assertThat(notification.getTo()).isEqualTo("user@test.com");
        assertThat(notification.getSubject()).isEqualTo("Test Subject");
        assertThat(notification.getMessage()).isEqualTo("This is a test message.");
    }

    @Test
    void gettersAndSetters_workAsExpected() {
        Notification notification = new Notification();
        notification.setSender("System");
        notification.setTo("user@test.com");
        notification.setSubject("Test Subject");
        notification.setMessage("Test Message");

        assertThat(notification.getSender()).isEqualTo("System");
        assertThat(notification.getTo()).isEqualTo("user@test.com");
        assertThat(notification.getSubject()).isEqualTo("Test Subject");
        assertThat(notification.getMessage()).isEqualTo("Test Message");
    }
}