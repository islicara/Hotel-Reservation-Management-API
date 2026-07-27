package de.lhind.internship.mini.project.dto;

import jakarta.validation.constraints.NotNull;

public class UpdateStatusDTO<T> {
    @NotNull(message = "Status cannot be null")
    private T status;

    public UpdateStatusDTO() {}

    public UpdateStatusDTO(T status) {
        this.status = status;
    }

    public @NotNull(message = "Status cannot be null") T getStatus() {
        return status;
    }

    public void setStatus(@NotNull(message = "Status cannot be null") T status) {
        this.status = status;
    }
}
