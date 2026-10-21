package slotly.demo.model.domain;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"date", "start"}))
public class Slot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;
    private LocalTime start;
    private int duration;

    private String bookedName;
    private String bookedEmail;

    private String service;
    private String notes;

    public Long getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getStart() {
        return start;
    }

    public int getDuration() {
        return duration;
    }

    public String getBookedName() {
        return bookedName;
    }

    public String getBookedEmail() {
        return bookedEmail;
    }

    public String getService() {
        return service;
    }

    public String getNotes() {
        return notes;
    }

    public boolean isBooked() {
        return bookedName != null;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setStart(LocalTime start) {
        this.start = start;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
