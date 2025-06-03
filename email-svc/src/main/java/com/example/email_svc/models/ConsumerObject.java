package com.example.email_svc.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConsumerObject {

    private String eventType;

    private String transactionId;

    private String fromAccount;

    private String toAccount;

    private String from;

    private String amount;

    private String timestamp;

}
