package com.example.email_svc.service;

import com.example.email_svc.models.ConsumerObject;
import com.example.email_svc.models.StatementDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendMail(ConsumerObject consumerObject) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(consumerObject.getToAccount());
        message.setSubject("🔔 Transaction Notification: " + consumerObject.getEventType());

        String body = buildEmailBody(consumerObject);
        message.setText(body);

        javaMailSender.send(message);
    }

    private String buildEmailBody(ConsumerObject obj) {
        return String.format(
                "Dear User,\n\n" +
                        "A new transaction event has occurred:\n\n" +
                        "📌 Event Type: %s\n" +
                        "🔁 Transaction ID: %s\n" +
                        "💳 From Account: %s\n" +
                        "🏦 To Account: %s\n" +
                        "💰 Amount: ₹%s\n" +
                        "⏰ Timestamp: %s\n\n" +
                        "If you did not authorize this transaction, please contact support immediately.\n\n" +
                        "Thank you,\n" +
                        "Your Bank",
                obj.getEventType(),
                obj.getTransactionId(),
                obj.getFromAccount(),
                obj.getToAccount(),
                obj.getAmount(),
                obj.getTimestamp()
        );
    }

    public void sendEmailStatement(StatementDTO statementDTO) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        simpleMailMessage.setTo(statementDTO.getUsername());
        simpleMailMessage.setSubject("Your Monthly Statement is Ready");

        String body = "Dear User,\n\n"
                + "Your monthly statement (" + statementDTO.getType() + ") is now available.\n"
                + "You can view or download it using the link below:\n\n"
                + statementDTO.getUrl() + "\n\n"
                + "Thank you,\n";

        simpleMailMessage.setText(body);

        javaMailSender.send(simpleMailMessage);
    }
}
