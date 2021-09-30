package org.thebreak.roombooking.common.util;

import java.math.BigDecimal;

public class PriceUtils {
    public static BigDecimal intToBigDecimalInDollar(int amount) {
        return BigDecimal.valueOf(amount).divide(new BigDecimal(100));
    }

    public static String formatDollarString(int amount) {
        if(amount < 10){
           return "0.0" + amount;
        } else if((amount < 100)) {
            return "0." + amount;
        } else {
            String amountString = amount + "";
            amountString = amountString.substring(0, amountString.length() - 2) + "." + amountString.substring(amountString.length() - 2);
            return amountString;
        }
    }

    public static int BigDecimalToIntInCent(BigDecimal amount) {
        BigDecimal centAmount = amount.multiply(new BigDecimal(100));
        return centAmount.intValue();
    }
}
