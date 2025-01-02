package be.pxl.services.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


import java.util.ArrayList;
import java.util.List;


@AllArgsConstructor
public class Notification {

    private Long id;
    private String sender;
    private String to;
    private String subject;
    private String message;

    public Notification() {
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

