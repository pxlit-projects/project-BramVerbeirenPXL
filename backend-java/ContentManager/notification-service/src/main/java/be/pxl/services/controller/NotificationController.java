package be.pxl.services.controller;

import be.pxl.services.domain.Notification;
import be.pxl.services.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void sendMessage(@RequestBody Notification notification) {
        notificationService.sendMessage(notification);
    }
}
