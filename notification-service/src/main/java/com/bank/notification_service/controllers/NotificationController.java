package com.bank.notification_service.controllers;

import com.bank.notification_service.enums.NotificationType;
import com.bank.notification_service.service.NotificationService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService service;

    @PostMapping("/statement/{type}/{id}")
    public ResponseEntity<String> getStatement(@PathVariable String type, @PathVariable String id) {
        try {
            NotificationType notificationType = NotificationType.valueOf(type.toUpperCase());
            service.statementNotification(notificationType, id);

            return ResponseEntity.ok("📧 Statement will be sent shortly to your email.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid notification type. Use DAILY, WEEKLY, or MONTHLY.");
        }
    }
}

