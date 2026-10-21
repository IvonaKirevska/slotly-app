package slotly.demo.model.dto;

import slotly.demo.model.domain.Slot;

import java.time.LocalDate;
import java.time.LocalTime;

public record SlotResponse(Long id, LocalDate date, LocalTime start, int duration, boolean booked) {
    public static SlotResponse from(Slot s) {
        return new SlotResponse(s.getId(), s.getDate(), s.getStart(), s.getDuration(), s.isBooked());
    }
}
