package com.bank.notification_service.service;

import com.bank.notification_service.enums.NotificationType;
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
            tableRows.append(String.format("""
            <tr>
                <td style="padding: 8px; border: 1px solid #ccc;">%s</td>
                <td style="padding: 8px; border: 1px solid #ccc;">%s %s</td>
                <td style="padding: 8px; border: 1px solid #ccc;">%s</td>
                <td style="padding: 8px; border: 1px solid #ccc;">%s</td>
                <td style="padding: 8px; border: 1px solid #ccc;">%s</td>
            </tr>
        """,
                    txn.getId(),
                    txn.getAmount(), txn.getCurrencyCode(),
                    txn.getFromAccount(),
                    txn.getToAccount(),
                    txn.getStatus()
            ));
        }

        String htmlContent = String.format("""
        <html>
        <body style="font-family: Arial, sans-serif; line-height: 1.6;">
            <h2 style="color: #2E86C1;">%s</h2>
            <p>Hello <strong>%s</strong>,</p>
            <p>Here is your %s transaction summary:</p>

            <table style="border-collapse: collapse; width: 100%%; margin-top: 10px;">
                <thead>
                    <tr style="background-color: #f2f2f2;">
                        <th style="padding: 8px; border: 1px solid #ccc;">Transaction ID</th>
                        <th style="padding: 8px; border: 1px solid #ccc;">Amount</th>
                        <th style="padding: 8px; border: 1px solid #ccc;">From</th>
                        <th style="padding: 8px; border: 1px solid #ccc;">To</th>
                        <th style="padding: 8px; border: 1px solid #ccc;">Status</th>
                    </tr>
                </thead>
                <tbody>
                    %s
                </tbody>
            </table>

            <p>Thank you,<br/>Bank Notification Service</p>
        </body>
        </html>
        """,
                subject,
                user.getUsername(),
                type.name().toLowerCase(),
                tableRows.toString()
        );

        try {
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(user.getEmail());
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // HTML content

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

        String htmlContent = String.format("""
        <html>
        <body style="font-family: Arial, sans-serif; line-height: 1.6;">
            <h2 style="color: #2E86C1;">Transaction Alert</h2>
            <p>Hello <strong>%s</strong>,</p>
            <p>Your transaction has been processed successfully. Below are the details:</p>

            <table style="border-collapse: collapse; width: 100%%; margin-top: 10px;">
                <tr>
                    <td style="padding: 8px; border: 1px solid #ccc;"><strong>Transaction ID</strong></td>
                    <td style="padding: 8px; border: 1px solid #ccc;">%s</td>
                </tr>
                <tr>
                    <td style="padding: 8px; border: 1px solid #ccc;"><strong>Amount</strong></td>
                    <td style="padding: 8px; border: 1px solid #ccc;">%s %s</td>
                </tr>
                <tr>
                    <td style="padding: 8px; border: 1px solid #ccc;"><strong>From</strong></td>
                    <td style="padding: 8px; border: 1px solid #ccc;">%s</td>
                </tr>
                <tr>
                    <td style="padding: 8px; border: 1px solid #ccc;"><strong>To</strong></td>
                    <td style="padding: 8px; border: 1px solid #ccc;">%s</td>
                </tr>
                <tr>
                    <td style="padding: 8px; border: 1px solid #ccc;"><strong>Status</strong></td>
                    <td style="padding: 8px; border: 1px solid #ccc;">%s</td>
                </tr>
                <tr>
                    <td style="padding: 8px; border: 1px solid #ccc;"><strong>Initiated At</strong></td>
                    <td style="padding: 8px; border: 1px solid #ccc;">%s</td>
                </tr>
                <tr>
                    <td style="padding: 8px; border: 1px solid #ccc;"><strong>Completed At</strong></td>
                    <td style="padding: 8px; border: 1px solid #ccc;">%s</td>
                </tr>
            </table>

            <p>If you did not authorize this transaction, please <a href="#">contact support</a> immediately.</p>
            <p>Thank you,<br/>Bank Notification Service</p>
        </body>
        </html>
        """,
                user.getUsername(),
                transaction.getId(),
                transaction.getAmount(), transaction.getCurrencyCode(),
                transaction.getFromAccount(),
                transaction.getToAccount(),
                transaction.getStatus(),
                transaction.getInitiatedAt(),
                transaction.getCompletedAt()
        );

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
