package org.thebreak.roombooking.common.model;


public class BookingCloseEmailBO {

    private String toEmailAddress;
    private String customerName;
    private String roomTitle;
    private String StartTime;

    public BookingCloseEmailBO() {
    }

    public BookingCloseEmailBO(String toEmailAddress, String customerName, String roomTitle, String startTime) {
        this.toEmailAddress = toEmailAddress;
        this.customerName = customerName;
        this.roomTitle = roomTitle;
        StartTime = startTime;
    }

    public String getToEmailAddress() {
        return toEmailAddress;
    }

    public void setToEmailAddress(String toEmailAddress) {
        this.toEmailAddress = toEmailAddress;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getRoomTitle() {
        return roomTitle;
    }

    public void setRoomTitle(String roomTitle) {
        this.roomTitle = roomTitle;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    @Override
    public String toString() {
        return "BookingReminderEmailBO{" +
                "toEmailAddress='" + toEmailAddress + '\'' +
                ", customerName='" + customerName + '\'' +
                ", roomTitle='" + roomTitle + '\'' +
                ", StartTime='" + StartTime + '\'' +
                '}';
    }
}
