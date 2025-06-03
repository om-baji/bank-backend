package com.example.bank.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Set;

public class CurrencyManager {

    private static final Map<String, Integer> CURRENCY_DECIMALS = Map.of(
            "INR", 2,
            "USD", 2,
            "EUR", 2,
            "JPY", 0
    );

    public static int getDecimals(String currencyCode) {
        Integer decimals = CURRENCY_DECIMALS.get(currencyCode.toUpperCase());
        if (decimals == null) {
            throw new IllegalArgumentException("Unknown currency code: " + currencyCode);
        }
        return decimals;
    }

    public static void validateAmountScale(BigDecimal amount, String currencyCode) {
        int allowedDecimals = getDecimals(currencyCode);
        if (amount.scale() > allowedDecimals) {
            throw new IllegalArgumentException(
                    "Amount " + amount + " has more decimal places than allowed for " + currencyCode +
                            " (allowed: " + allowedDecimals + ")");
        }
    }

    public static long toRawAmount(BigDecimal amount, String currencyCode) {
        validateAmountScale(amount, currencyCode);
        int decimals = getDecimals(currencyCode);
        BigDecimal raw = amount.movePointRight(decimals).setScale(0, RoundingMode.UNNECESSARY);
        return raw.longValueExact();
    }

    public static BigDecimal fromRawAmount(long rawAmount, String currencyCode) {
        int decimals = getDecimals(currencyCode);
        return BigDecimal.valueOf(rawAmount).movePointLeft(decimals);
    }

    public static Set<String> getSupportedCurrencies() {
        return CURRENCY_DECIMALS.keySet();
    }
}

