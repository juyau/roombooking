package org.thebreak.roombooking.common.model;


public class BookingReminderEmailBO {

    private String toEmailAddress;
    private String customerName;
    private String roomTitle;
    private int totalHours;
    private String StartTime;


    public BookingReminderEmailBO(String toEmailAddress, String customerName, String roomTitle, int totalHours, String startTime) {
        this.toEmailAddress = toEmailAddress;
        this.customerName = customerName;
        this.roomTitle = roomTitle;
        this.totalHours = totalHours;
        StartTime = startTime;
    }

    public int getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(int totalHours) {
        this.totalHours = totalHours;
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

}
