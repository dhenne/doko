package de.vinode.dokobackend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


public record RoundPlayerResult(
        String name,
        Integer points) {

}
