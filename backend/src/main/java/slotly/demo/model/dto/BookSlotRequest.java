package slotly.demo.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import slotly.demo.model.enums.ServiceType;

public record BookSlotRequest(@NotBlank String name,
                              @NotBlank @Email String email,
                              @NotNull ServiceType service,
                              String notes) {
}
