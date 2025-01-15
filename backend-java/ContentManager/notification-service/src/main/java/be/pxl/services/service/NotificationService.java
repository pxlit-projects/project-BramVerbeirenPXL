package be.pxl.services.service;

import be.pxl.services.domain.Notification;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);
    @Autowired
    private JavaMailSender mailSender;

    public void sendMessage(Notification notification){
        log.info("Receiving notificiation...");
        log.info("Sending... {}", notification.getMessage());
        log.info("TO {}", notification.getSender());

        if(notification.getSender().equals("Review Service")){
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("bramverbeiren.2904@gmail.com");
            message.setTo(notification.getTo());
            message.setSubject(notification.getSubject());
            message.setText(notification.getMessage());

            mailSender.send(message);
            log.info("E-mail succesvol verzonden!");
        }

    }

}
