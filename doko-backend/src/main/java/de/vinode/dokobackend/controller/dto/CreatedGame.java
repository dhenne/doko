package de.vinode.dokobackend.controller.dto;

import lombok.*;

@Builder
public record CreatedGame(@NonNull String shareId) {
}
