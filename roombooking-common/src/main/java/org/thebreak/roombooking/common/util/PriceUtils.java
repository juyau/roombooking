package org.thebreak.roombooking.common.util;

import java.math.BigDecimal;

public class PriceUtils {
    public static BigDecimal longToBigDecimalInDollar(long amount) {
        BigDecimal decimal = BigDecimal.valueOf(amount).divide(new BigDecimal(100));
        return decimal;
    }

    public static String formatDollarString(long amount) {
        String amountString = amount + "";
        amountString = amountString.substring(0, amountString.length() - 2) + "." + amountString.substring(amountString.length() - 2);
        return amountString;
    }

    public static long BigDecimalToLongInCent(BigDecimal amount) {
        BigDecimal centAmount = amount.multiply(new BigDecimal(100));
        return centAmount.longValue();
    }
}
