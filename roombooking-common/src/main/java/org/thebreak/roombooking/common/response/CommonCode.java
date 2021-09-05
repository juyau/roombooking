package org.thebreak.roombooking.common.response;

public enum CommonCode {

    SUCCESS(true, 200, "OK"),
    FAILED(false, 9999, "Operation failed."),

    // system
    SYSTEM_ERROR(false, 9000, "system error."),
    INVALID_PARAM(false, 3000, "parameter not valid."),

    // request param check
    REQUEST_FIELD_MISSING(false, 4001, "Missing required field(s)."),
    REQUEST_FIELD_EMPTY(false, 4002, "Request included empty field(s)"),
    REQUEST_JSON_MISSING(false, 4004, "Missing required JSON data."),
    REQUEST_ID_FIELD_MISSING(false, 4005, "id field is required."),
    REQUEST_ID_INVALID_OR_EMPTY(false, 4006, "id is invalid or is empty."),
    REQUEST_FIELD_INVALID(false, 4003, "Request field(s) include invalid data"),

    // Room code
    ROOM_ENTRY_ALREADY_EXIST(false, 5001, "room with same address and room number already exist."),

    // Booking code
    BOOKING_TIME_EARLIER_THAN_NOW(false, 6001, "booking time cannot be a time in the past."),
    BOOKING_TIME_ALREADY_TAKEN(false, 6002, "booking time is already taken by other users."),
    BOOKING_TIMERANGE_NOTAVAILABLE(false, 6003, "booking time in the range is not available for this room."),
    BOOKING_WEEKEND_ONLY(false, 6004, "this room is available only for weekends."),
    BOOKING_WEEKDAY_ONLY(false, 6005, "this room is available only for weekdays."),
    BOOKING_TIME_QUARTER_ONLY(false, 6006, "start and end time must be in quarter."),
    BOOKING_TIME_HOURLY_ONLY(false, 6006, "booking time unit is in hour and must at least one hour."),
    BOOKING_END_BEFORE_START(false, 6007, "booking end time must later than start time."),
    BOOKING_PAID_WITHOUT_AMOUNT(false, 6008, "must provide positive paidAmount when change status to paid."),
    BOOKING_CONTACT_NOTNULL(false, 6009, "contact name, email, mobile are all required."),
    BOOKING_EMAIL_INVALID(false, 6010, "email invalid."),
    BOOKING_SENDEMAIL_FAILED(false, 6011, "failed to send notification email."),

    // payment code
    PAYMENT_START_MUST_BEFORE_END(false, 7001, "start date must not later than end date."),

    // database access code
    DB_ENTRY_ALREADY_EXIST(false, 3001, "entry already exist."),
    DB_EMPTY_LIST(true, 3002, "query list result is empty."),
    DB_DELETE_FAILED(false, 3003, "failed to delete entry."),
    DB_ENTRY_NOT_FOUND(false, 3004, "data entry not exist.");

    private final int code;
    private final String message;
    private final Boolean success;

    CommonCode(Boolean success, int code, String message) {
        this.code = code;
        this.message = message;
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Boolean getSuccess() {
        return success;
    }
}
