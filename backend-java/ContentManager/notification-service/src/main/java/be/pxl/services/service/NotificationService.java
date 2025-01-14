package be.pxl.services.service;

import be.pxl.services.domain.Notification;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    public void sendMessage(Notification notification){
        log.info("Receiving notificiation...");
        log.info("Sending... {}", notification.getMessage());
        log.info("TO {}", notification.getSender());
    }

}
