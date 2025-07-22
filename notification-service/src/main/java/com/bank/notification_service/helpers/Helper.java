package com.bank.notification_service.helpers;

import com.bank.notification_service.models.Transaction;
import org.springframework.stereotype.Component;

@Component
public class Helper {

    public String transactionTemplate(String username, Transaction transaction) {
        return String.format("""
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
                username,
                transaction.getId(),
                transaction.getAmount(), transaction.getCurrencyCode(),
                transaction.getFromAccount(),
                transaction.getToAccount(),
                transaction.getStatus(),
                transaction.getInitiatedAt(),
                transaction.getCompletedAt()
        );
    }

    public String statementTemplate(String subject,String username,String type,String tableRows) {
        return String.format("""
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
                username,
                type,
                tableRows
        );
    }

    public String txnTableTemplate(Transaction txn) {
        return String.format("""
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
        );
    }
}
