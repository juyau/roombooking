package org.thebreak.roombooking.app.model.enums;

public enum BookingCloseReasonEnum {

    PAYMENT_TIMEOUT(1, "Payment timeout"),
    OVERDUE(2, "overdue"),
    CANCELLED(3, "Cancelled");

    private final int code;
    private final String description;

    BookingCloseReasonEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
