package org.thebreak.roombooking.email.model;


import io.swagger.v3.oas.annotations.media.Schema;

public class EmailBO {

    @Schema(example = "philip@gmail.com")
    private String to;

    @Schema(example = "Booking Payment notification")
    private String subject;

    @Schema(example = "Dear user, Thank you for...")
    private String body;

    public EmailBO(String to, String subject, String body) {
        this.to = to;
        this.subject = subject;
        this.body = body;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
