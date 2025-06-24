package com.bank.user_service.schemas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.kafka.common.protocol.types.Field;

import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiSuccess {

    private boolean success;
    private String message;
}
