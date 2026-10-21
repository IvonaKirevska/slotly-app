package slotly.demo.model.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record CreateSlotRequest(
        @NotNull LocalDate date,
        @NotNull LocalTime start,
        @Min(15) @Max(480) int duration
) {}
