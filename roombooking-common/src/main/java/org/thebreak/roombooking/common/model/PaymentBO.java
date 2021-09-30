package org.thebreak.roombooking.common.model;



public class PaymentBO {
    private String bookingId;
    private int amount;
    private String transId;
    private int type;

    public PaymentBO() {
    }

    public PaymentBO(String bookingId, int amount, String transId, int type) {
        this.bookingId = bookingId;
        this.amount = amount;
        this.transId = transId;
        this.type = type;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "PaymentBO{" +
                "bookingId='" + bookingId + '\'' +
                ", amount=" + amount +
                ", transId='" + transId + '\'' +
                ", type=" + type +
                '}';
    }
}
