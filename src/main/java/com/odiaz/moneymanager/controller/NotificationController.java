package com.odiaz.moneymanager.controller;

import com.odiaz.moneymanager.service.NotificationService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }



}
