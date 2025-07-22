package com.bank.notification_service.service;

import com.bank.notification_service.enums.NotificationType;
import com.bank.notification_service.helpers.Helper;
import com.bank.notification_service.models.Transaction;
import com.bank.notification_service.models.UserLookup;
import com.bank.notification_service.repository.TransactionRepository;
import com.bank.notification_service.repository.UserLookupRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class NotificationService {

    @Autowired
    private UserLookupRepository userLookupRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private JavaMailSender sender;

    @Autowired
    private Helper helper;

    public void statementNotification(NotificationType type, String userId) {
        Optional<UserLookup> exist = userLookupRepository.findById(userId);

        if (exist.isEmpty()) {
            log.warn("User not found for userId: {}", userId);
            return;
        }

        UserLookup user = exist.get();
        List<Transaction> transactions = transactionRepository.findAllByUserId(userId);

        if (transactions.isEmpty()) {
            log.info("No transactions found for user: {}", userId);
            return;
        }

        String subject = switch (type) {
            case DAILY -> "Your Daily Transaction Summary";
            case WEEKLY -> "Your Weekly Transaction Summary";
            case MONTHLY -> "Your Monthly Transaction Statement";
        };

        StringBuilder tableRows = new StringBuilder();
        for (Transaction txn : transactions) {
            tableRows.append(helper.txnTableTemplate(txn));
        }

        String htmlContent = helper.statementTemplate(subject,user.getUsername(),
                type.toString(),tableRows.toString());

        try {
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(user.getEmail());
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            sender.send(message);
            log.info("Statement notification sent to {}", user.getEmail());
        } catch (MessagingException e) {
            log.error("Failed to send statement notification email", e);
        }
    }

    public void transactionNotification(Transaction transaction) {

        Optional<UserLookup> exist = userLookupRepository.findById(transaction.getUserId());

        if (exist.isEmpty()) {
            log.warn("User not found for userId: {}", transaction.getUserId());
            return;
        }

        UserLookup user = exist.get();

        String subject = "Transaction Confirmation - Bank Notification Service";

        String htmlContent = helper.transactionTemplate(user.getUsername(),transaction);

        try {
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(user.getEmail());
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            sender.send(message);

            log.info("HTML email sent to {}", user.getEmail());
        } catch (MessagingException e) {
            log.error("Failed to send HTML email", e);
        }
    }
}