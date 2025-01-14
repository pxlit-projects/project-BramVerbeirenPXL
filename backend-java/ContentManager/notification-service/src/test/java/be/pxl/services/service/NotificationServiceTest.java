package be.pxl.services.service;

import be.pxl.services.domain.Notification;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class NotificationServiceTest {

    private NotificationService notificationService;

    @BeforeEach
    void setup() {
        notificationService = new NotificationService();
    }

    @Test
    void sendMessage_logsNotification() {
        // Maak een ListAppender aan om logberichten op te vangen
        Logger logger = (Logger) LoggerFactory.getLogger(NotificationService.class);
        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        logger.addAppender(listAppender);

        // Testdata
        Notification notification = new Notification(1L, "System", "user@test.com", "Test Subject", "This is a test message.");

        // Roep de service aan
        notificationService.sendMessage(notification);

        // Haal de logberichten op
        List<ILoggingEvent> logsList = listAppender.list;

        // Controleer dat de juiste logberichten zijn aangemaakt
        assertThat(logsList).hasSize(3);
        assertThat(logsList.get(0).getFormattedMessage()).isEqualTo("Receiving notificiation...");
        assertThat(logsList.get(1).getFormattedMessage()).isEqualTo("Sending... This is a test message.");
        assertThat(logsList.get(2).getFormattedMessage()).isEqualTo("TO System");
    }
}
